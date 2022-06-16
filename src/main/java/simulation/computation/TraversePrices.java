package simulation.computation;

import simulation.environment.Epochs;
import simulation.vilages.Village;

/**
 * Traversal based on calculated price indexes.
 */
public class TraversePrices extends TraverseBase {

  /**
   * Constructor, grabs map.
   * @param epochs Epochs.
   */
  public TraversePrices(Epochs epochs) {
    super(epochs);
  }

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
