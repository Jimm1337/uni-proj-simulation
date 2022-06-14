package simulation.computation;

import simulation.environment.Epochs;
import simulation.goods.Product;
import simulation.goods.ProductType;
import simulation.goods.Transaction;
import simulation.goods.TransactionType;
import simulation.player.PlayerStorage;
import simulation.strategy.StrategyType;
import simulation.vilages.Village;

import java.util.*;

public class SellingAlgorithm {
  private static final float PERCENT_TO_SELL_OFF = 0.9f;
  private static final float[] TRANSACTION_RATIOS = {0.4f, 0.3f, 0.2f, 0.1f};

  PlayerStorage playerStorage;
  StrategyType strategyType;

  private static SellingAlgorithm instance;

  public SellingAlgorithm() {
    Epochs epochs = Epochs.getInstance();
    this.strategyType = epochs.getStrategyType();
    playerStorage = PlayerStorage.getInstance();
  }

  public List<Transaction> generateTransactions(Village village) {
    List<Transaction> ret = new ArrayList<>(ProductType.values().length);

    Map<ProductType, Float> weights = generateWeights(village);

    for (ProductType type : ProductType.values()) {
      float weightToSell = weights.get(type);
      Product productToSell = new Product(type, weightToSell);
      float price = village.getPrice(type);

      Transaction toAdd = new Transaction(price, productToSell, TransactionType.SELL, strategyType);
      ret.add(toAdd);
    }

    return ret;
  }

  private Set<Map.Entry<Float, ProductType>> getDescendingPricesSet(Village village) {
    Map<Float, ProductType> prices = new TreeMap<>(Comparator.reverseOrder());
    for (ProductType type : ProductType.values()) {
      float price = village.getPrice(type);
      prices.put(price, type);
    }
    return prices.entrySet();
  }

  private Map<ProductType, Float> generatePercentages(Village village) {
    Map<ProductType, Float> ret = new HashMap<>();

    //insert into returning map with generated descending prices set
    int i = 0;
    for (Map.Entry<Float, ProductType> entry : getDescendingPricesSet(village)) {
      float percentage = TRANSACTION_RATIOS[i];
      ProductType type = entry.getValue();
      ret.put(type, percentage);
      ++i;
    }

    return ret;
  }

  private Map<ProductType, Float> generateWeights(Village village) {
    Map<ProductType, Float> ret = new HashMap<>();

    Map<ProductType, Float> percentages = generatePercentages(village);
    Map<ProductType, Product> playerStock = playerStorage.getStock();
    float villageMoney = village.getMoney();

    //put into return either the target to sell or whatever village can afford
    int i = 0;
    for (Map.Entry<ProductType, Float> entry : percentages.entrySet()) {
      ProductType type = entry.getKey();
      Product stock = playerStock.get(type);
      float price = village.getPrice(type);
      float targetWeightToSell = stock.getWeight() * PERCENT_TO_SELL_OFF;
      float villageCanAfford = villageMoney / price;
      ret.put(type, Math.min(targetWeightToSell, villageCanAfford));
      ++i;
    }

    return ret;
  }
}
