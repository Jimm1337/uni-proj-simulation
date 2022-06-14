package io.console;

import simulation.computation.TraverseBase;
import simulation.computation.TraverseDistance;
import simulation.computation.TraversePrices;
import simulation.strategy.AggressiveStrategy;
import simulation.strategy.BalancedStrategy;
import simulation.strategy.ConservativeStrategy;
import simulation.strategy.StrategyType;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.Console;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles input, both direct and Enter-confirmed
 */
public class Input {
  Map<Integer, Boolean> keysPressed;
  KeyboardFocusManager kbFocusMgr;
  Console console;

  /**
   * Constructor, grabs kb handle, inits key status map
   */
  public Input() {
    keysPressed = new HashMap<>();
    console = System.console();

    kbFocusMgr = KeyboardFocusManager.getCurrentKeyboardFocusManager();

    kbFocusMgr.addKeyEventDispatcher(ev -> {
      synchronized (Input.class) {
        int keyCode = ev.getKeyCode();
        switch (ev.getID()) {
          case KeyEvent.KEY_PRESSED -> keysPressed.put(keyCode, true);
          case KeyEvent.KEY_RELEASED -> keysPressed.put(keyCode, false);
        }
      }
      return false;
    });
  }

  /**
   * Check if a key is currently pressed.
   * @param keyCode KeyEvent.VK_[X]
   * @return true if currently pressed.
   */
  public boolean isCurrentlyPressed(int keyCode) {
    return keysPressed.getOrDefault(keyCode, false);
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
        return new BalancedStrategy();
      }
      case "a" -> {
        return new AggressiveStrategy();
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
        return new TraversePrices();
      }
      case "d" -> {
        return new TraverseDistance();
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
