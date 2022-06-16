package simulation.computation;

import java.util.Random;

/**
 * Dice class for rolling random events.
 */
public class Dice {
  private final Random generator;

  /**
   * Constructor, creates a new Random obj for internal use.
   */
  public Dice() {
    this.generator = new Random();
  }

  /**
   * Rolling method.
   * @param chance Chance for an event to happen.
   * @return True if the event ought to happen.
   */
  public boolean roll(float chance) {
    float rollResult = generator.nextFloat();

    return rollResult <= chance;
  }
}
