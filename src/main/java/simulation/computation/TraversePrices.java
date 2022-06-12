package simulation.computation;

import simulation.vilages.Village;

/**
 * Traversal based on calculated price indexes.
 */
public class TraversePrices extends TraverseBase {

  /**
   * Get the next village to travel to.
   * @return Next Village based on traversal strategy.
   */
  @Override
  public Village getNext() {
    Village next = map.getBestPrices();
    return next;
  }
}
