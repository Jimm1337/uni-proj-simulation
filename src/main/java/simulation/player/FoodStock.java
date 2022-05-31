package simulation.player;

/**
 * Handles the stock of food during the simulation.
 */
public class FoodStock {
  private static final int INITIAL_STOCK = 100;
  private static final int INITIAL_CONSUMPTION = 15;

  private int consumption;
  private int stock;

  private static FoodStock instance;

  /**
   * Singleton constructor.
   */
  private FoodStock() {
    consumption = INITIAL_CONSUMPTION;
    stock = INITIAL_STOCK;
  }

  /**
   * Singleton instance getter. Creates instance if it doesn't yet exist.
   * @return The only instance of FoodStock
   */
  public FoodStock getInstance() {
    if (instance == null) {
      instance = new FoodStock();
    }
    return instance;
  }

  /**
   * Updates the daily food consumption.
   * @param consumption New value of daily consumption.
   */
  public void setConsumption(int consumption) {
    this.consumption = consumption;
  }

  /**
   * Adds to the food stock.
   * @param units Units of food to be added.
   */
  public void addToStock(int units) {
    stock += units;
  }

  /**
   * Consumes the current consumption value.
   */
  public void consume() {
    stock -= consumption;
  }

  /**
   * Gets the current stock of food. Used to determine if the simulation should end.
   * @return Current stock.
   */
  public int getStock() {
    return stock;
  }
}
