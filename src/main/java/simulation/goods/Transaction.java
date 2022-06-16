package simulation.goods;

import simulation.environment.Epochs;
import simulation.strategy.StrategyType;
import simulation.vilages.Village;

/**
 * Represents a single transaction.
 */
public class Transaction {
  private final float           price;
  private final Product         product;
  private final TransactionType transactionType;
  private final StrategyType    strategyType;
  private final Epochs epochs;

  /**
   * Initialize the transaction with details.
   * @param price The price of unit of product.
   * @param product The product representation.
   * @param transactionType The type of the transaction.
   * @param strategyType The selected strategy type.
   * @param epochs Epochs.
   */
  public Transaction(
    float           price,
    Product         product,
    TransactionType transactionType,
    StrategyType    strategyType,
    Epochs epochs) {
    this.epochs = epochs;
    this.price           = price;
    this.product         = product;
    this.transactionType = transactionType;
    this.strategyType    = strategyType;
  }

  /**
   * Price getter.
   * @return Price of product unit.
   */
  public float getPrice() {
    return price;
  }

  /**
   * Product getter.
   * @return Subject of the transaction.
   */
  public Product getProduct() {
    return product;
  }

  /**
   * Type getter.
   * @return Type of the product.
   */
  public TransactionType getTransactionType() {
    return transactionType;
  }

  /**
   * Calculate the total transaction monetary value.
   * @return Price * Weight of Product.
   */
  public float getTotal() {
    return price * product.getWeight();
  }

  /**
   * Executes the transaction.
   * @param village Village partner.
   * @throws IllegalArgumentException When the transaction cannot be fulfilled.
   */
  public void execute(Village village) {
    StockBase   playerStorage   = epochs.getPlayerStorage();
    final float priceMultiplier = strategyType.getPriceMultiplier();
    final float value           = getTotal() * priceMultiplier;

    switch (transactionType) {
      case BUY -> {
        village.subtractProduct(product);
        village.addMoney(value);
        playerStorage.subtractMoney(value); //throws
        playerStorage.addProduct(product);
      }
      case SELL -> {
        village.addProduct(product);
        village.subtractMoney(value);
        playerStorage.addMoney(value);
        playerStorage.subtractProduct(product); //throws
      }
    }
  }
}
