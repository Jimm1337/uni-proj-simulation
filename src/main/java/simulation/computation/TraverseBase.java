package simulation.computation;

import simulation.environment.VillageMap;
import simulation.vilages.Village;

/**
 * Base for traversal strategies.
 */
public abstract class TraverseBase {
  VillageMap map;

  /**
   * Constructor, grabs map.
   */
  public TraverseBase() {
    map = VillageMap.getInstance();
  }

  /**
   * Get the next village to travel to.
   * @return Next Village based on traversal strategy.
   */
  public abstract Village getNext();
}
