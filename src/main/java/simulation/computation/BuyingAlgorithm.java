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
 * Algorithm used to buy goods at a village.
 */
public class BuyingAlgorithm {
  private static final float FOOD_THRESHOLD          = 30.0f;
  private static final float MAX_PERCENT_MONEY_SPENT = 0.8f;
  private static final float[] TRANSACTION_RATIOS = { 0.4f, 0.3f, 0.2f, 0.1f };

  private final PlayerStorage playerStorage;
  private final StrategyType  strategyType;
  private final Epochs epochs;

  /**
   * Grabs strategy and player storage ref.
   * @param epochs Epochs.
   */
  public BuyingAlgorithm(Epochs epochs) {
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

    for (ProductType type : weights.keySet()) {
      float   weightToBuy  = weights.get(type);
      Product productToBuy = new Product(type, weightToBuy);
      float   price        = village.getPrice(type);

      Transaction toAdd =
        new Transaction(price, productToBuy, TransactionType.BUY, strategyType, epochs);
      ret.add(toAdd);
    }

    return ret;
  }

  /**
   * Internal, grabs prices form village in ascending order.
   * @param village village partner.
   * @return Set of Price-Type pairs.
   */
  private Set<Map.Entry<Float, ProductType>> getAscendingPricesSet(
    Village village) {
    Map<Float, ProductType> prices = new TreeMap<>();
    for (ProductType type : ProductType.values()) {
      float price = village.getPrice(type);
      prices.put(price, type);
    }
    return prices.entrySet();
  }

  /**
   * Internal, generates of products to buy.
   * @param village Village partner.
   * @return Map of types and percentages.
   */
  private Map<ProductType, Float> generatePercentages(Village village) {
    Map<ProductType, Float> ret = new HashMap<>();

    final Product foodStock       = playerStorage.getProduct(ProductType.FOOD);
    final float   foodStockWeight = foodStock.getWeight();

    // if food below threshold buy only food
    if (foodStockWeight <= FOOD_THRESHOLD) {
      ret.put(ProductType.FOOD, 1.0f);
      return ret;
    }

    // insert into returning map with generated ascending prices set
    int i = 0;
    for (Map.Entry<Float, ProductType> entry : getAscendingPricesSet(village)) {
      float       percentage = TRANSACTION_RATIOS[i];
      ProductType type       = entry.getValue();
      ret.put(type, percentage);
      ++i;
    }

    return ret;
  }

  /**
   * Internal, generates exact weights of products to buy.
   * @param village Village partner.
   * @return Map of ProductTypes mapped to weights.
   */
  private Map<ProductType, Float> generateWeights(Village village) {
    Map<ProductType, Float> ret = new HashMap<>();

    Map<ProductType, Float>   percentages  = generatePercentages(village);
    Map<ProductType, Product> villageStock = village.getStock();
    float                     playerMoney  = playerStorage.getMoney();

    // put into return either the stock or whatever player can afford
    int i = 0;
    for (Map.Entry<ProductType, Float> entry : percentages.entrySet()) {
      ProductType type        = entry.getKey();
      Product     stock       = villageStock.get(type);
      float       stockWeight = stock.getWeight();
      float       price       = village.getPrice(type);
      float       canSpend =
        playerMoney * MAX_PERCENT_MONEY_SPENT * TRANSACTION_RATIOS[i];
      float targetWeight = canSpend / price;
      ret.put(type, Math.min(targetWeight, stockWeight));
      ++i;
    }

    return ret;
  }
}
