package simulation.strategy;

/**
 * The least random type of simulation.
 */
public class ConservativeStrategy implements StrategyType {
  private static final int   foodConsumption = 10;
  private static final float priceMultiplier = 1.0f;
  private static final float travelCost      = 1.15f;

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
