package io.arguments;

/**
 * Used to parse console arguments from the user.
 */
public class Parser {
  private Difficulty difficulty;

  /**
   * Parsing method.
   * @param Args main() function arguments.
   * @throws IllegalArgumentException Wrong number of program arguments.
   * @throws IllegalArgumentException Argument rng out of range.
   * @throws IllegalArgumentException Argument steal multiplier out of range.
   */
  public void parse(String[] Args) {
    if (Args.length != 2) {
      throw new IllegalArgumentException("Too few Arguments, expected 2.");
    }

    int rng = Integer.parseInt(Args[0]);
    if (rng < 2 || rng > 6) {
      throw new IllegalArgumentException(
        "Rng out of range - allowed range <2,6>.");
    }

    float stealMulti = Float.parseFloat(Args[1]);
    if (stealMulti < 0.5 || stealMulti > 2.0) {
      throw new IllegalArgumentException(
        "Steal multiplier out of range - allowed range <0.5, 2.0>.");
    }

    difficulty = new Difficulty(rng, stealMulti);
  }

  /**
   * Difficulty getter will throw NULLPTR if not parse was not called first.
   * @return Parsed args as Difficulty object.
   */
  public Difficulty getDifficulty() {
    return difficulty;
  }
}
