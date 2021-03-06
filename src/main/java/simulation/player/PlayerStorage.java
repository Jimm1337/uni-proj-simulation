package simulation.player;

import simulation.environment.Epochs;
import simulation.goods.*;
import simulation.strategy.StrategyType;

/**
 * Handles the storage of goods, food and money through the simulation.
 */
public class PlayerStorage extends StockBase implements TransactionChecker {
  private static final float   INITIAL_MONEY = 100.0f;
  private static final Product INITIAL_FOOD =
    new Product(ProductType.FOOD, 300.0f);

  private final Epochs epochs;

  /**
   * Initializes the Map to HashMap with All the product
   * types.
   * @param epochs Epochs.
   */
  public PlayerStorage(Epochs epochs) {
    this.epochs = epochs;

    for (ProductType type : ProductType.values()) {
      addProduct(new Product(type, 0.0f));
    }
    addMoney(INITIAL_MONEY);
    addProduct(INITIAL_FOOD);
  }

  /**
   * Check if transaction is possible (enough money or stock).
   * @param transaction transaction to check.
   * @return true if transaction is valid.
   */
  @Override
  public boolean isTransactionPossible(Transaction transaction) {
    float           total           = transaction.getTotal();
    Product         product         = transaction.getProduct();
    TransactionType transactionType = transaction.getTransactionType();
    ProductType     productType     = product.getType();

    switch (transactionType) {
      case BUY -> {
        boolean hasEnoughMoney = getMoney() >= total;
        return hasEnoughMoney;
      }
      case SELL -> {
        float stockWeight = getProduct(productType).getWeight();
        float askingWeight = product.getWeight();
        boolean hasEnoughStock = stockWeight >= askingWeight;
        return hasEnoughStock;
      }
    }

    return false;
  }

  /**
   * Consumes daily food or dies if not enough food left.
   */
  public void consumeDailyFood() {
    StrategyType strategy = epochs.getStrategyType();

    float consumption = strategy.getFoodConsumption();

    Product foodStock = getProduct(ProductType.FOOD);
    float foodStockWeight = foodStock.getWeight();

    if (foodStockWeight < consumption) {
      PlayerState playerState = epochs.getPlayerState();
      playerState.die();
      return;
    }

    Product toConsume = new Product(ProductType.FOOD, consumption);
    subtractProduct(toConsume);
  }
}
