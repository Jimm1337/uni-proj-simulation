package simulation.vilages;

import io.arguments.Difficulty;
import simulation.environment.Position;
import simulation.goods.*;

import java.util.Map;

/**
 * Represents a single Village.
 */
public class Village extends StockBase implements TransactionChecker {
  private static final float STOCK_RAND_MULTI = 20.0f;
  private static final float MONEY_RAND_MULTI = 1000.0f;
  private static final float PRICE_RAND_MULTI = 10.0f;

  private final Position position;
  private Map<ProductType, Float> prices;
  private final float priceIndex;

  /**
   * Initialize with random stock and prices. Set Position.
   * @param position Position of the village.
   */
  public Village(Position position) {
    this.position = position;
    randomizeStock();
    randomizePrices();
    priceIndex = calculatePriceIndex();
  }

  /**
   * Randomly change stock.
   */
  public void randomizeStock() {
    Difficulty randomMachine = Difficulty.getInstance();
    clearStock();
    clearMoney();

    for (ProductType type : ProductType.values()) {
      float randomWeight = randomMachine.getRandomFloat() * STOCK_RAND_MULTI;
      Product productToAdd = new Product(type, randomWeight);
      addProduct(productToAdd);
    }

    addMoney(randomMachine.getRandomFloat() * MONEY_RAND_MULTI);
  }

  /**
   * Randomly change prices.
   */
  public void randomizePrices() {
    Difficulty randomMachine = Difficulty.getInstance();
    clearPrices();

    for (ProductType type : ProductType.values()) {
      float randomPrice = randomMachine.getRandomFloat() * PRICE_RAND_MULTI;
      addPrice(type, randomPrice);
    }
  }

  /**
   * Get current Price for Product Type.
   * @param productType Type of the product.
   * @return Price per unit of Product.
   */
  public float getPrice(ProductType productType) {
    return prices.get(productType);
  }

  /**
   * Position getter
   * @return Village position.
   */
  public Position getPosition() {
    return position;
  }

  /**
   * Add price to the price table.
   * @param productType Type of product.
   * @param price Price per unit.
   */
  private void addPrice(ProductType productType, float price) {
    prices.put(productType, price);
  }

  /**
   * Clear the price table.
   */
  private void clearPrices() {
    prices.clear();
  }

  /**
   * Check if transaction is possible (enough money or stock).
   *
   * @param transaction transaction to check.
   * @return true if transaction is valid.
   */
  @Override
  public boolean isTransactionPossible(Transaction transaction) {
    float total = transaction.getTotal();
    Product product = transaction.getProduct();
    TransactionType transactionType = transaction.getTransactionType();
    ProductType productType = product.getType();

    switch (transactionType) {
      case BUY -> {
        float askingWeight = product.getWeight();
        float stockWeight = getProduct(productType).getWeight();
        boolean hasEnoughStock = stockWeight >= askingWeight;
        return hasEnoughStock;
      }
      case SELL -> {
        boolean hasEnoughMoney = getMoney() >= total;
        return hasEnoughMoney;
      }
    }

    return false;
  }

  /**
   * Calculate the price index.
   * @return Simple average of prices.
   */
  private float calculatePriceIndex() {
    float sumOfPrices = 0.0f;
    for (float price : prices.values()) {
      sumOfPrices += price;
    }
    float simpleAverage = sumOfPrices / prices.size();

    return simpleAverage;
  }

  /**
   * Price index getter.
   * @return Simple Average of prices.
   */
  public float getPriceIndex() {
    return priceIndex;
  }
}
