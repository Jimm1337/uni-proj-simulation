package io.console;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles input, both direct and Enter-confirmed
 */
public class Input {
  Map<Integer, Boolean> keysPressed;
  KeyboardFocusManager kbFocusMgr;

  /**
   * Constructor, grabs kb handle, inits key status map
   */
  public Input() {
    keysPressed = new HashMap<>();

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
   * Read enter-confirmed command.
   * @return parsed Command.
   * @throws IllegalArgumentException Wrong param type.
   * @throws IllegalStateException Wrong command code.
   */
  public Command readCommand() {
    String read = getStringEntered();
    String[] split = read.split(" ");
    String commandCode = split[0];
    CommandType commandType = CommandType.byCode(commandCode);

    if (commandType == CommandType.INVALID) {
      throw new IllegalStateException("Invalid command code.");
    }

    if (split.length > 1) {
      String rawParam = split[1];

      if (commandType == CommandType.ADVANCE_BY) {
        try {
          int parsedRawParam = Integer.parseInt(rawParam);
          Param<?> param = new Param<>(parsedRawParam);
          return new Command(commandType, param);
        } catch (NumberFormatException err) {
          throw new IllegalArgumentException("Wrong param type.");
        }
      }

      Param<?> param = new Param<>(rawParam);
      return new Command(commandType, param);
    }

    return new Command(commandType);
  }

  /**
   * Internal impl of reading an enter-confirmed sequence
   * @return Resulting sequence.
   */
  private String getStringEntered() {
    String read = System.console().readLine();

    return read;
  }
}
