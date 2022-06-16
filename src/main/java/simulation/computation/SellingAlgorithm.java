package simulation.computation;

import java.util.*;
import simulation.environment.Epochs;
import simulation.goods.Product;
import simulation.goods.ProductType;
import simulation.goods.Transaction;
import simulation.goods.TransactionType;
import simulation.player.PlayerStorage;
import simulation.strategy.StrategyType;
import simulation.vilages.Village;

/**
 * Selling algorithm used in the simulation.
 */
public class SellingAlgorithm {
  private static final float PERCENT_TO_SELL_OFF  = 0.9f;
  private static final float[] TRANSACTION_RATIOS = { 0.4f, 0.3f, 0.2f, 0.1f };

  private final PlayerStorage playerStorage;
  private final StrategyType  strategyType;
  private final Epochs epochs;

  /**
   * Grabs strategy and player storage ref.
   * @param epochs Epochs.
   */
  public SellingAlgorithm(Epochs epochs) {
    this.epochs = epochs;
    this.strategyType = epochs.getStrategyType();
    playerStorage     = epochs.getPlayerStorage();
  }

  /**
   * Generates a List of Transaction based on the algorithm.
   * @param village Village transaction partner.
   * @return List of Transactions to be made.
   */
  public List<Transaction> generateTransactions(Village village) {
    List<Transaction> ret = new ArrayList<>(ProductType.values().length);

    Map<ProductType, Float> weights = generateWeights(village);

    for (var entry : weights.entrySet()) {
      ProductType type = entry.getKey();
      float   weightToSell  = weights.get(type);
      Product productToSell = new Product(type, weightToSell);
      float   price         = village.getPrice(type);

      Transaction toAdd = new Transaction(
        price, productToSell, TransactionType.SELL, strategyType, epochs);
      ret.add(toAdd);
    }

    return ret;
  }

  /**
   * Internal, grabs prices form village in descending order.
   * @param village village partner.
   * @return Set of Price-Type pairs.
   */
  private Set<Map.Entry<Float, ProductType>> getDescendingPricesSet(
    Village village) {
    Map<Float, ProductType> prices = new TreeMap<>(Comparator.reverseOrder());
    for (ProductType type : ProductType.values()) {
      if (type == ProductType.FOOD) continue;
      float price = village.getPrice(type);
      prices.put(price, type);
    }

    return prices.entrySet();
  }

  /**
   * Internal, generates of products to sell.
   * @param village Village partner.
   * @return Map of types and percentages.
   */
  private Map<ProductType, Float> generatePercentages(Village village) {
    Map<ProductType, Float> ret = new HashMap<>();

    // insert into returning map with generated descending prices set
    int i = 0;
    for (Map.Entry<Float, ProductType> entry :
         getDescendingPricesSet(village)) {
      float       percentage = PERCENT_TO_SELL_OFF * TRANSACTION_RATIOS[i];
      ProductType type       = entry.getValue();
      ret.put(type, percentage);
      ++i;
    }

    return ret;
  }

  /**
   * Internal, generates exact weights of products to sell.
   * @param village Village partner.
   * @return Map of ProductTypes mapped to weights.
   */
  private Map<ProductType, Float> generateWeights(Village village) {
    Map<ProductType, Float> ret = new HashMap<>();

    Map<ProductType, Float>   percentages  = generatePercentages(village);
    Map<ProductType, Product> playerStock  = playerStorage.getStock();
    float                     villageMoney = village.getMoney();

    // put into return either the target to sell or whatever village can afford
    int i = 0;
    for (Map.Entry<ProductType, Float> entry : percentages.entrySet()) {
      ProductType type               = entry.getKey();
      Product     stock              = playerStock.get(type);
      float       toSellFraq         = entry.getValue();
      float       price              = village.getPrice(type);
      float       targetWeightToSell = stock.getWeight() * toSellFraq;
      float villageCanAfford = (villageMoney / price) * TRANSACTION_RATIOS[0];
      ret.put(type, Math.min(targetWeightToSell, villageCanAfford));
      ++i;
    }

    return ret;
  }
}
