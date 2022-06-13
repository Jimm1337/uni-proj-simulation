package simulation.goods;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Base class for storage.
 */
public abstract class StockBase {
  private Map<ProductType, Product> stock;
  private float                     money;

  /**
   * Constructor. Initializes stock HashMap.
   */
  public StockBase() {
    stock = new HashMap<>();
  }

  /**
   * Clear the stock.
   */
  public void clearStock() {
    stock.clear();
  }

  /**
   * Reset money.
   */
  public void clearMoney() {
    money = 0.0f;
  }

  /**
   * Add Product to stock.
   * @param toAdd The Product to add.
   */
  public void addProduct(Product toAdd) {
    ProductType typeToAdd   = toAdd.getType();
    float       weightToAdd = toAdd.getWeight();

    stock.get(typeToAdd).addWeight(weightToAdd);
  }

  /**
   * Subtracts the Product from stock.
   * It is advised to check if the stock holds enough of the product to
   * subtract.
   * @param toSubtract Product representation.
   * @throws IllegalArgumentException When the stock isn't enough.
   */
  public void subtractProduct(Product toSubtract) {
    ProductType typeToSubtract   = toSubtract.getType();
    float       weightToSubtract = toSubtract.getWeight();
    Product     working          = stock.get(typeToSubtract);

    if (working.getWeight() < toSubtract.getWeight()) {
      throw new IllegalArgumentException(
          "Weight After subtraction cannot be negative.");
    }
    working.addWeight(weightToSubtract);
  }

  /**
   * Gets the product by type.
   * @param type Product type to get.
   * @return Product current stock.
   */
  public Product getProduct(ProductType type) {
    return stock.get(type);
  }

  /**
   * Get the stock
   * @return Map of Products.
   */
  public Map<ProductType, Product> getStock() {
    return stock;
  }

  /**
   * Gets the current amount of money.
   * @return The current amount of money.
   */
  public float getMoney() {
    return money;
  }

  /**
   * Adds to the current amount of money.
   * @param toAdd The amount to add.
   */
  public void addMoney(float toAdd) {
    money += toAdd;
  }

  /**
   * Subtracts from the current amount of money.
   * It is Advised to check whether the remaining amount of money is enough.
   * @param toSubtract The amount of money to subtract.
   * @throws IllegalArgumentException When the stock of money is not enough.
   */
  public void subtractMoney(float toSubtract) {
    if (money < toSubtract) {
      throw new IllegalArgumentException(
          "Money amount after subtraction cannot be negative.");
    }
    money -= toSubtract;
  }
}
