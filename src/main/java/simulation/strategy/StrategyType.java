package simulation.strategy;

/**
 * Allows for different strategies chosen at runtime.
 */
public interface StrategyType {
  /**
   * Gets daily food consumption in units of food.
   * @return daily food consumption in units of food.
   */
  float getFoodConsumption();

  /**
   * Gets price multiplier for transactions.
   * @return price multiplier for transactions.
   */
  float getPriceMultiplier();

  /**
   * Gets travel cost per unit of distance.
   * @return travel cost per unit of distance.
   */
  float getTravelCost();

  /**
   * Randomly change food consumption based on generator and difficulty
   * settings. By default, does nothing.
   */
  default void fluctuateFoodConsumption() {
  }

  /**
   * Randomly change price multiplier based on generator and difficulty
   * settings. By default, does nothing.
   */
  default void fluctuatePriceMultiplier() {
  }
}
