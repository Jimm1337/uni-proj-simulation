package io.console;

/**
 * Errors related to controller io.
 */
public enum ErrorMsg {
  /**
   * Invalid Epochs JSON file.
   */
  INVALID_FILE,
  /**
   * Invalid strategy type.
   */
  INVALID_STRATEGY,
  /**
   * Invalid traverse algorithm.
   */
  INVALID_TRAVERSE,
  /**
   * Invalid command/argument combination.
   */
  INVALID_COMMAND,
  /**
   * Error while saving to a file.
   */
  ERROR_SAVE;

  /**
   * get String representation.
   * @return String representation of this ErrorMsg.
   */
  @Override
  public String toString() {
    switch (this) {
      case INVALID_FILE -> {
        return "Invalid file!";
      }
      case INVALID_STRATEGY -> {
        return "Invalid strategy!";
      }
      case INVALID_TRAVERSE -> {
        return "Invalid traverse method!";
      }
      case INVALID_COMMAND -> {
        return "Invalid command-argument combination!";
      }
      case ERROR_SAVE -> {
        return "Problems saving to a file. Try again.";
      }
    }
    return "";
  }
}
