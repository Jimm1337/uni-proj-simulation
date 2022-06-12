package simulation.computation;

import simulation.environment.VillageMap;
import simulation.vilages.Village;

/**
 * Traversal based on distance from the player.
 */
public class TraverseDistance extends TraverseBase {

  /**
   * Get the next village to travel to.
   * @return Next Village based on traversal strategy.
   */
  @Override
  public Village getNext() {
    Village next = map.getClosestToPlayer();
    return next;
  }
}
