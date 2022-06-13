package io.console;

import simulation.environment.Epochs;

/**
 * Controls the program flow, executes commands.
 */
public class Controller {
  Input input;
  Output output;
  Epochs epochs;

  /**
   * Constructor, creates io, grabs Epochs (main simulation class) instance.
   */
  public Controller() {
    this.input = new Input();
    this.output = new Output();
    this.epochs = Epochs.getInstance();
  }

  /**
   * Start controlling the simulation.
   */
  public void entry() {
    Command entryCommand = new Command(CommandType.ENTRY);
    executeCommand(entryCommand);
  }

  /**
   * Execute a given command.
   * @param command The command to be executed.
   * @throws IllegalArgumentException When the param doesn't is of invalid type for given command type.
   */
  private void executeCommand(Command command) {
    CommandType commandType = command.getCommandType();
    Param<?> param = command.getParam();
    var paramValue = param.getValue();

    switch (commandType) {
      case GET_COMMAND -> {
        handleGetCommand();
      }
      case ENTRY -> {
        handleEntry();
      }
      case RESUME -> {
        String filename;
        try {
          filename = (String)paramValue;
        } catch (Throwable err) {
          throw new IllegalArgumentException("Wrong argument type for command resume.");
        }
        handleResume(filename);
      }
      case SET_STRATEGY -> {
        handleSetStrategy();
      }
      case SET_TRAVERSAL -> {
        handleSetTraversal();
      }
      case ADVANCE -> {
        handleAdvance();
      }
      case ADVANCE_BY -> {
        Integer by;
        try {
          by = (Integer)paramValue;
        } catch (Throwable err) {
          throw new IllegalArgumentException("Wrong argument type for command AdvanceBy.");
        }
        handleAdvanceBy(by);
      }
      case ADVANCE_AUTO -> {
        handleAdvanceAuto();
      }
      case ADVANCE_STOP -> {
        handleAdvanceStop();
      }
      case SAVE_QUIT -> {
        String filename;
        try {
          filename = (String)paramValue;
        } catch (Throwable err) {
          throw new IllegalArgumentException("Wrong argument type for command saveQuit.");
        }
        handleSaveQuit(filename);
      }
      case QUIT -> {
        handleQuit();
      }
      case HELP -> {
        handleHelp();
      }
    }
  }

  // COMMAND HANDLERS // //todo

  private void handleGetCommand() {

  }

  private void handleEntry() {

  }

  private void handleResume(String filename) {

  }

  private void handleSetStrategy() {

  }

  private void handleSetTraversal() {

  }

  private void handleAdvance() {

  }

  private void handleAdvanceBy(int by) {

  }

  private void handleAdvanceAuto() {

  }

  private void handleAdvanceStop() {

  }

  private void handleSaveQuit(String filename) {

  }

  private void handleQuit() {

  }

  private void handleHelp() {

  }
}
