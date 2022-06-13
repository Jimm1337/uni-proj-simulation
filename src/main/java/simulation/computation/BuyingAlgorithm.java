package simulation.computation;

import simulation.goods.Product;
import simulation.goods.ProductType;
import simulation.goods.Transaction;
import simulation.goods.TransactionType;
import simulation.player.PlayerStorage;
import simulation.strategy.StrategyType;
import simulation.vilages.Village;

import java.util.*;

public class BuyingAlgorithm {
  private static final float FOOD_THRESHOLD = 30.0f;
  private static final float MAX_PERCENT_MONEY_SPENT = 0.8f;
  private static final float[] TRANSACTION_RATIOS = {0.4f, 0.3f, 0.2f, 0.1f};

  PlayerStorage playerStorage;
  StrategyType strategyType;
  Village village;

  public BuyingAlgorithm(Village village, StrategyType strategyType) {
    this.village = village;
    this.strategyType = strategyType;
    playerStorage = PlayerStorage.getInstance();
  }

  public List<Transaction> generateTransactions() {
    List<Transaction> ret = new ArrayList<>(ProductType.values().length);

    Map<ProductType, Float> weights = generateWeights();

    for (ProductType type : ProductType.values()) {
      float weightToBuy = weights.get(type);
      Product productToBuy = new Product(type, weightToBuy);
      float price = village.getPrice(type);

      Transaction toAdd = new Transaction(price, productToBuy, TransactionType.BUY, strategyType);
      ret.add(toAdd);
    }

    return ret;
  }

  private Set<Map.Entry<Float, ProductType>> getAscendingPricesSet() {
    Map<Float, ProductType> prices = new TreeMap<>();
    for (ProductType type : ProductType.values()) {
      float price = village.getPrice(type);
      prices.put(price, type);
    }
    return prices.entrySet();
  }

  private Map<ProductType, Float> generatePercentages() {
    Map<ProductType, Float> ret = new HashMap<>();

    final Product foodStock = playerStorage.getProduct(ProductType.FOOD);
    final float foodStockWeight = foodStock.getWeight();

    //if food below threshold buy only food
    if (foodStockWeight <= FOOD_THRESHOLD) {
      ret.put(ProductType.FOOD, 1.0f);
      return ret;
    }

    //insert into returning map with generated ascending prices set
    int i = 0;
    for (Map.Entry<Float, ProductType> entry : getAscendingPricesSet()) {
      float percentage = TRANSACTION_RATIOS[i];
      ProductType type = entry.getValue();
      ret.put(type, percentage);
      ++i;
    }

    return ret;
  }

  private Map<ProductType, Float> generateWeights() {
    Map<ProductType, Float> ret = new HashMap<>();

    Map<ProductType, Float> percentages = generatePercentages();
    Map<ProductType, Product> villageStock = village.getStock();
    float playerMoney = playerStorage.getMoney();

    //put into return either the stock or whatever player can afford
    int i = 0;
    for (Map.Entry<ProductType, Float> entry : percentages.entrySet()) {
      ProductType type = entry.getKey();
      Product stock = villageStock.get(type);
      float stockWeight = stock.getWeight();
      float price = village.getPrice(type);
      float canSpend = playerMoney * MAX_PERCENT_MONEY_SPENT * TRANSACTION_RATIOS[i];
      float targetWeight = canSpend / price;
      ret.put(type, Math.min(targetWeight, stockWeight));
      ++i;
    }

    return ret;
  }
}
