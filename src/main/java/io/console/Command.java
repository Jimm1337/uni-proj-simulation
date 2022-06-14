package io.console;

/**
 * Represents a command to be given to the controller
 */
class Command {
  private final CommandType type;
  private final Param<?> param;

  /**
   * Type only constructor, for commands with no params.
   * @param type the type of command.
   */
  Command(CommandType type) {
    this.type  = type;
    this.param = null;
  }

  /**
   * Constructor with type and param.
   * @param type the type of command.
   * @param param param to be passed to the command.
   */
  Command(CommandType type, Param<?> param) {
    this.type  = type;
    this.param = param;
  }

  /**
   * Command type getter.
   * @return The type of the command.
   */
  CommandType getCommandType() {
    return type;
  }

  /**
   * Param getter.
   * @return param to be processed.
   */
  Param<?> getParam() {
    return param;
  }
}
