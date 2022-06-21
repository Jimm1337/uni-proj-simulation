package io.console;

/**
 * Types of commands available to the simulation controller. With Runtime manual
 * description.
 */
enum CommandType {
  /**
   * Get next command. (internal)
   */
  GET_COMMAND,
  /**
   * Simulation entry. (internal)
   */
  ENTRY,
  /**
   * Resume from file. (internal)
   */
  RESUME,
  /**
   * Set strategy type. (internal)
   */
  SET_STRATEGY,
  /**
   * Set traverse algorithm. (internal)
   */
  SET_TRAVERSAL,
  /**
   * Advance by 1 epoch. (user)
   */
  ADVANCE,
  /**
   * Advance by n epochs. (user)
   */
  ADVANCE_BY,
  /**
   * Save and quit. (user)
   */
  SAVE_QUIT,
  /**
   * Quit. (user and internal)
   */
  QUIT,
  /**
   * Display the manual. (user)
   */
  HELP,
  /**
   * Invalid state.
   */
  INVALID;

  private static final String NO_MANUAL = "";

  /**
   * Grab manual for each type.
   * @return If command ought to be used by user return a description else
   *   return ""
   */
  public String manualEntry() {
    switch (this) {
      case ADVANCE -> {
        return "(a<Enter>) |Advance| - advance one epoch.\n";
      }
      case ADVANCE_BY -> {
        return "(ab :COUNT<Enter>) |Advance by| - advance by a fixed count of epochs.\n";
      }
      case SAVE_QUIT -> {
        return "(sq :FILENAME<Enter>) |Save Quit| - save current state and exit.\n";
      }
      case QUIT -> {
        return "(qq<Enter>) |Quit| - quit without saving.\n";
      }
      case HELP -> {
        return "(h<Enter>) |Help| - display this manual.\n";
      }
      default -> {
        return NO_MANUAL; //internal use commands
      }
    }
  }

  /**
   * Get CommandType by code.
   * @param code string command code.
   * @return Command Type of the code or INVALID
   */
  public static CommandType byCode(String code) {
    switch (code) {
      case "a" -> {
        return ADVANCE;
      }
      case "ab" -> {
        return ADVANCE_BY;
      }
      case "sq" -> {
        return SAVE_QUIT;
      }
      case "qq" -> {
        return QUIT;
      }
      case "h" -> {
        return HELP;
      }
      default -> {
        return INVALID; //internal use commands
      }
    }
  }
}
