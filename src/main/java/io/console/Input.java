package io.console;

import java.io.Console;
import simulation.computation.TraverseBase;
import simulation.computation.TraverseDistance;
import simulation.computation.TraversePrices;
import simulation.environment.Epochs;
import simulation.strategy.AggressiveStrategy;
import simulation.strategy.BalancedStrategy;
import simulation.strategy.ConservativeStrategy;
import simulation.strategy.StrategyType;

/**
 * Handles input, both direct and Enter-confirmed
 */
public class Input {
  private final Console console;
  private final Epochs epochs;

  /**
   * Constructor, grabs kb handle, inits key status map
   * @param epochs Epochs.
   */
  public Input(Epochs epochs) {
    console = System.console();
    this.epochs = epochs;
  }

  /**
   * Read y/n block
   * @return true if yes.
   */
  public Boolean readYesNo() {
    String read = getStringEntered();
    switch (read) {
      case "y" -> {
        return true;
      }
      case "n" -> {
        return false;
      }
      default -> {
        return null;
      }
    }
  }

  /**
   * Read strategy selection block.
   * @return selected strategy or null if not valid.
   */
  public StrategyType readStrategy() {
    String read = getStringEntered();
    switch (read) {
      case "c" -> {
        return new ConservativeStrategy();
      }
      case "b" -> {
        return new BalancedStrategy(epochs);
      }
      case "a" -> {
        return new AggressiveStrategy(epochs);
      }
      default -> {
        return null;
      }
    }
  }

  /**
   * Read traverse algorithm selection block.
   * @return selected algorithm or null if not valid.
   */
  public TraverseBase readTraverse() {
    String read = getStringEntered();
    switch (read) {
      case "p" -> {
        return new TraversePrices(epochs);
      }
      case "d" -> {
        return new TraverseDistance(epochs);
      }
      default -> {
        return null;
      }
    }
  }

  /**
   * Read enter-confirmed command.
   * @return parsed Command or null if invalid.
   */
  public Command readCommand() {
    String read = getStringEntered();
    String[] split = read.split(" ");
    String commandCode = split[0];
    CommandType commandType = CommandType.byCode(commandCode);

    if (commandType == CommandType.INVALID) {
      return null;
    }

    if (split.length > 1) {
      String rawParam = split[1];

      if (commandType == CommandType.ADVANCE_BY) {
        try {
          int parsedRawParam = Integer.parseInt(rawParam);
          Param<?> param = new Param<>(parsedRawParam);
          return new Command(commandType, param);
        } catch (NumberFormatException err) {
          return null;
        }
      }

      Param<?> param = new Param<>(rawParam);
      return new Command(commandType, param);
    }

    return new Command(commandType);
  }

  /**
   * Read an enter-confirmed sequence.
   * @return Resulting sequence.
   */
  public String getStringEntered() {
    String read = console.readLine();

    return read;
  }
}
