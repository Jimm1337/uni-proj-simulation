package simulation.vilages;

import io.arguments.Difficulty;
import simulation.goods.Product;
import simulation.goods.ProductType;
import simulation.player.PlayerStorage;

/**
 * Thugs class, used to calculate and proceed with loses in a theft event.
 */
public class Thugs {
  private static final float BASE_STEAL_SEVERITY = 0.45f;

  private final float stealPercent;

  /**
   * Constructor, calculates the percentage of resource to be stolen in an
   * event.
   */
  public Thugs() {
    Difficulty  difficulty = Difficulty.getInstance();
    final float stealMulti = difficulty.getStealMultiplier();
    stealPercent           = BASE_STEAL_SEVERITY * stealMulti;
  }

  /**
   * Steal action to be executed in a theft event. Subtracts percentage of
   * resource calculated in the constructor from current stock.
   */
  public void steal() {
    PlayerStorage playerStorage = PlayerStorage.getInstance();

    float currentMoney = playerStorage.getMoney();
    float moneyToSteal = currentMoney * stealPercent;
    playerStorage.subtractMoney(moneyToSteal);

    for (ProductType type : ProductType.values()) {
      Product current       = playerStorage.getProduct(type);
      float   currentWeight = current.getWeight();
      float   weightToSteal = currentWeight * stealPercent;
      Product toSteal       = new Product(type, weightToSteal);

      playerStorage.subtractProduct(toSteal);
    }
  }
}
