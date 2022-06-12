package simulation.strategy;

import io.arguments.Difficulty;
import java.util.Random;

/**
 * The midpoint of simulation randomness.
 */
public class BalancedStrategy implements StrategyType {
  private static final int FOOD_RAND_MULTI = 1;

  private static final float priceMultiplier = 0.9f;
  private static final float travelCost      = 1.05f;
  private int                foodConsumption = 15;
  private final Difficulty   difficulty;

  /**
   * Balanced Strategy constructor. Grabs Difficulty class instance.
   */
  public BalancedStrategy() {
    this.difficulty = Difficulty.getInstance();
  }

  /**
   * Randomly change food consumption based on generator and difficulty
   * settings.
   */
  @Override
  public void fluctuateFoodConsumption() {
    foodConsumption += difficulty.getRandomInt() * FOOD_RAND_MULTI;
  }

  /**
   * Gets daily food consumption in units of food.
   * @return daily food consumption in units of food.
   */
  @Override
  public int getFoodConsumption() {
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
