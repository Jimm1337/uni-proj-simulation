package simulation.computation;

import simulation.environment.Epochs;
import simulation.vilages.Village;

/**
 * Traversal based on distance from the player.
 */
public class TraverseDistance extends TraverseBase {
  /**
   * Constructor, grabs map.
   * @param epochs Epochs.
   */
  public TraverseDistance(Epochs epochs) {
    super(epochs);
  }

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
