package io.arguments;

import java.util.Random;

/**
 * Defines multipliers known at the start of the simulation.
 */
public class Difficulty {
  private static final float RANGE_MULTI = 0.02f;

  private final int          rng;
  private final float        stealMultiplier;

  /**
   * Generates Difficulty from arguments.
   * @param rng rng param (1st param - reasonable range -> From 2 To 6
   *   inclusive).
   * @param stealMultiplier sm param (2nd param - reasonable range -> From 0.5
   *   To 2.0 inclusive).
   */
  Difficulty(int rng, float stealMultiplier) {
    this.rng             = rng;
    this.stealMultiplier = stealMultiplier;
  }

  /**
   * Gets random int based on rng argument.
   * @return Random int based on rng argument and random engine.
   */
  public int getRandomInt() {
    Random randEngine = new Random();
    return randEngine.nextInt() % rng;
  }

  /**
   * Gets random float based on rng argument.
   * @return Random float based on rng argument and random engine.
   */
  public float getRandomFloat() {
    float  range      = RANGE_MULTI * (float)rng;
    Random randEngine = new Random();
    float  generated  = randEngine.nextFloat() % range;

    if (randEngine.nextBoolean()) generated *= -1.0f;

    return generated;
  }

  /**
   * Gets steal multiplier.
   * @return Steal Multiplier parsed from args.
   */
  public float getStealMultiplier() {
    return stealMultiplier;
  }
}
