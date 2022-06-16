package simulation.computation;

import simulation.environment.Epochs;
import simulation.environment.VillageMap;
import simulation.vilages.Village;

/**
 * Base for traversal strategies.
 */
public abstract class TraverseBase {
  /**
   * Village map to use in the algorithms.
   */
  protected final VillageMap map;

  /**
   * Constructor, grabs map.
   * @param epochs Epochs.
   */
  public TraverseBase(Epochs epochs) {
    map = epochs.getVillageMap();
  }

  /**
   * Get the next village to travel to.
   * @return Next Village based on traversal strategy.
   */
  public abstract Village getNext();
}
