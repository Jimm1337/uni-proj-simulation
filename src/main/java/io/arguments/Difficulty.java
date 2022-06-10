package io.arguments;

import java.util.Random;

/**
 * Defines multipliers known at the start of the simulation. Single instance
 */
public class Difficulty {
  private final int          rng;
  private final float        stealMultiplier;
  static Difficulty instance;

  /**
   * Private constructor, construct using createInstance()
   * @param rng rng from createInstance
   * @param stealMultiplier stealMultiplier from createInstance()
   */
  private Difficulty(int rng, float stealMultiplier) {
    this.rng             = rng;
    this.stealMultiplier = stealMultiplier;
  }

  /**
   * Generates Difficulty from arguments. Only for use in parser class.
   * @param rng rng param (1st param - reasonable range -> From 2 To 6
   *   inclusive).
   * @param stealMultiplier sm param (2nd param - reasonable range -> From 0.5
   *   To 2.0 inclusive).
   */
  static void createInstance(int rng, float stealMultiplier) {
    if (instance == null) {
      instance = new Difficulty(rng, stealMultiplier);
    }
  }

  /**
   * Gets the only instance of Difficulty produced by the parser. If the parser failed to produce valid Difficulty object throws.
   * @throws RuntimeException When the parser didn't produce a valid instance at the start of the program.
   * @return The only Difficulty instance
   */
  public static Difficulty getInstance() {
    if (instance == null) {
      throw new RuntimeException("Failed to parse args.");
    }
    return instance;
  }

  /**
   * Gets random int from -rng to rng based on rng argument.
   * @return Random int based on rng argument and random engine.
   */
  public int getRandomInt() {
    Random randEngine = new Random();
    return randEngine.nextInt() % rng;
  }

  /**
   * Gets random float from -rng to rng based on rng argument.
   * @return Random float based on rng argument and random engine.
   */
  public float getRandomFloat() {
    float  range      = (float)rng;
    Random randEngine = new Random();
    float  generated  = randEngine.nextFloat() * range;

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
