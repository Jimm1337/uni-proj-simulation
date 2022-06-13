package io.console;

/**
 * Types of commands available to the simulation controller. With Runtime manual description.
 */
enum CommandType {
  GET_COMMAND,
  ENTRY,
  RESUME,
  SET_STRATEGY,
  SET_TRAVERSAL,
  ADVANCE,
  ADVANCE_BY,
  ADVANCE_AUTO,
  ADVANCE_STOP,
  SAVE_QUIT,
  QUIT,
  HELP;

  private static final String NO_MANUAL = "";

  /**
   * Grab manual for each type.
   * @return If command ought to be used by user return a description, else return ""
   */
  public String manualEntry() {
    switch (this) {
      case ADVANCE -> {
        return "(a<Enter>) |Advance| - advance one epoch.\n";
      }
      case ADVANCE_BY -> {
        return "(ab :COUNT<Enter>) |Advance by| - advance by a fixed count of epochs.\n";
      }
      case ADVANCE_AUTO -> {
        return "(aa<Enter>) |Advance auto| - keep advancing until an Advance stop is received.\n";
      }
      case ADVANCE_STOP -> {
        return "(s - when in Advance auto) |Advance stop| - stop advancing.\n";
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
}
