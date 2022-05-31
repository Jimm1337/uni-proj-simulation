package simulation.goods;

import simulation.player.Storage;

/**
 * Represents a single transaction.
 */
public class Transaction {
  private final float           price;
  private final Product         product;
  private final TransactionType type;

  /**
   * Initialize the transaction with details.
   * @param price The price of unit of product.
   * @param product The product representation.
   * @param type The type of the transaction.
   */
  public Transaction(float price, Product product, TransactionType type) {
    this.price   = price;
    this.product = product;
    this.type    = type;
  }

  /**
   * Executes the transaction.
   * @throws IllegalArgumentException When the transaction cannot be fulfilled.
   */
  public void execute() {
    Storage     storage = Storage.getInstance();
    final float value   = price * product.getWeight();

      switch (type) {
      case BUY -> {
        storage.subtractMoney(value); //throws
        storage.addProduct(product);
      }
      case SELL -> {
        storage.addMoney(value);
        storage.subtractProduct(product); //throws
      }
    }
  }
}
