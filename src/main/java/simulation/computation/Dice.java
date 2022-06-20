package simulation.computation;

import java.util.Random;

/**
 * Dice class for rolling random events.
 */
public class Dice {
  /**
   * Rolling method.
   * @param chance Chance for an event to happen.
   * @return True if the event ought to happen.
   */
  public boolean roll(float chance) {
    Random generator = new Random();

    float rollResult = generator.nextFloat();

    return rollResult <= chance;
  }
}
