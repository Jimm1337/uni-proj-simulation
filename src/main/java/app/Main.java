package app;

import io.arguments.Parser;
import io.console.Controller;

/**
 * Application entry point.
 */
public class Main {
  /**
   * Start of the simulation.
   * @param Args Application arguments.
   */
  public static void main(String[] Args) {
    Parser argParser = new Parser();

    try {
      argParser.parse(Args);
    } catch (IllegalArgumentException err) {
      System.out.println("""
          Usage: ./gradlew run --args "<RNG> <StealMulti>"
          
          int RNG - the value of randomness in the simulation. (range <2, 6>)
          float StealMulti - The multiplier for amount lost during random theft event. (range <0.5, 2.0>)
      """);

      return;
    }

    Controller consoleController = new Controller();
    consoleController.entry();
  }
}

//todo: (3) reduce singletons to just Epochs class.
//todo: (2) impl JSON, save state
//todo: (1) adjust ratios, fix thugs, fix clear screen
