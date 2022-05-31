package io.arguments;

/**
 * Used to parse console arguments from the user.
 */
public class Parser {
  /**
   * Parsing method.
   * @param Args main() function arguments.
   * @return Difficulty instance with parsed arguments.
   * @throws IllegalArgumentException Wrong number of program arguments.
   * @throws IllegalArgumentException Argument rng out of range.
   * @throws IllegalArgumentException Argument steal multiplier out of range.
   */
  public Difficulty parse(String[] Args) {
    if (Args.length != 2) {
      throw new IllegalArgumentException("Too few Arguments, expected 2");
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

    return new Difficulty(rng, stealMulti);
  }
}
