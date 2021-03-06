package simulation.strategy;

import io.arguments.Difficulty;
import simulation.environment.Epochs;

/**
 * The most random type of simulation.
 */
public class AggressiveStrategy implements StrategyType {
  private static final float FOOD_RAND_MULTI  = 1.0f;
  private static final float PRICE_RAND_MULTI = 0.01f;

  private static final float travelCost      = 1.0f;
  private float              foodConsumption = 20.0f;
  private float              priceMultiplier = 0.7f;
  private final Difficulty   difficulty;

  /**
   * Aggressive strategy constructor. Grabs Difficulty class instance.
   * @param epochs Epochs.
   */
  public AggressiveStrategy(Epochs epochs) {
    this.difficulty = epochs.getDifficulty();
  }

  /**
   * Randomly change food consumption based on generator and difficulty
   * settings.
   */
  @Override
  public void fluctuateFoodConsumption() {
    foodConsumption = 15.0f + difficulty.getRandomFloat() * FOOD_RAND_MULTI;
  }

  /**
   * Randomly change price multiplier based on generator and difficulty
   * settings.
   */
  @Override
  public void fluctuatePriceMultiplier() {
    priceMultiplier = 0.7f + difficulty.getRandomFloat() * PRICE_RAND_MULTI;
  }

  /**
   * Gets daily food consumption in units of food.
   * @return daily food consumption in units of food.
   */
  @Override
  public float getFoodConsumption() {
    return foodConsumption;
  }

  /**
   * Gets price multiplier for transactions.
   * @return price multiplier for transactions.
   */
  @Override
  public float getPriceMultiplier() {
    return priceMultiplier;
  }

  /**
   * Gets travel cost per unit of distance.
   * @return travel cost per unit of distance.
   */
  @Override
  public float getTravelCost() {
    return travelCost;
  }
}
