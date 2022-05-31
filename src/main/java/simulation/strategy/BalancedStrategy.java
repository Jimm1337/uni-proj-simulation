package simulation.strategy;

import io.arguments.Difficulty;
import java.util.Random;

/**
 * The midpoint of simulation randomness.
 */
public class BalancedStrategy implements StrategyType {
  private static final float priceMultiplier = 0.9f;
  private static final float travelCost      = 1.05f;
  private int                foodConsumption = 15;
  private final Difficulty   difficulty;

  /**
   * Balanced Strategy constructor.
   * @param difficulty Difficulty object from parsed args.
   */
  public BalancedStrategy(Difficulty difficulty) {
    this.difficulty = difficulty;
  }

  /**
   * Randomly change food consumption based on generator and difficulty
   * settings.
   */
  @Override
  public void fluctuateFoodConsumption() {
    foodConsumption += difficulty.getRandomInt();
  }

  /**
   * Gets daily food consumption in units of food.
   *
   * @return daily food consumption in units of food.
   */
  @Override
  public int getFoodConsumption() {
    return foodConsumption;
  }

  /**
   * Gets price multiplier for transactions.
   *
   * @return price multiplier for transactions.
   */
  @Override
  public float getPriceMultiplier() {
    return priceMultiplier;
  }

  /**
   * Gets travel cost per unit of distance.
   *
   * @return travel cost per unit of distance.
   */
  @Override
  public float getTravelCost() {
    return travelCost;
  }
}
