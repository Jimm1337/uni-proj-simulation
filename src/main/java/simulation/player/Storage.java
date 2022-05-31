package simulation.player;

import java.util.*;
import simulation.goods.Product;
import simulation.goods.ProductType;

/**
 * Handles the storage of goods, food and money through the simulation.
 */
public class Storage {
  private static final float        INITIAL_MONEY = 100.0f;
  private static Storage            instance;
  private Map<ProductType, Product> stock;
  private float                     money;

  /**
   * Singleton constructor. Initializes the Map to HashMap with All the product
   * types.
   */
  private Storage() {
    stock = new HashMap<>();
      for (ProductType type : ProductType.values()) {
        stock.put(type, new Product(type, 0.0f));
      }
    money = INITIAL_MONEY;
  }

  /**
   * Gets the singleton only instance. Initializes if necessary.
   * @return The instance.
   */
  public static Storage getInstance() {
      if (instance == null) { instance = new Storage(); }
    return instance;
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
      throw new IllegalArgumentException("Weight After subtraction cannot be negative.");
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
