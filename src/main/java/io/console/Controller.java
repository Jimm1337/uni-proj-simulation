package io.console;

import io.arguments.Difficulty;
import io.json.Converter;
import simulation.computation.TraverseBase;
import simulation.environment.Epochs;
import simulation.strategy.StrategyType;

/**
 * Controls the program flow, executes commands.
 */
public class Controller {
  private Input           input;
  private Output          output;
  private Epochs          epochs;
  private Command         nextCommand;
  private final Converter converter;

  /**
   * Constructor, creates io, grabs Epochs (main simulation class) instance.
   * @param difficulty Difficulty.
   */
  public Controller(Difficulty difficulty) {
    this.epochs      = new Epochs(difficulty);
    this.input       = new Input(epochs);
    this.output      = new Output(epochs);
    this.nextCommand = new Command(CommandType.ENTRY);
    this.converter   = new Converter();
  }

  /**
   * Start controlling the simulation. Spin while nextCommand isn't set to null.
   */
  public void entry() {
    while (nextCommand != null) {
      try {
        executeCommand(nextCommand);
      } catch (IllegalArgumentException err) {
        output.setError(ErrorMsg.INVALID_COMMAND);
        executeCommand(new Command(CommandType.GET_COMMAND));
      }
    }
  }

  /**
   * Execute a given command.
   * @param command The command to be executed.
   * @throws IllegalArgumentException When the param doesn't is of invalid type
   *   for given command type.
   */
  private void executeCommand(Command command) {
    CommandType commandType = command.getCommandType();
    Param<?>    param       = command.getParam();

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
          filename = (String)param.getValue();
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
          by = (Integer)param.getValue();
        } catch (Throwable err) {
          throw new IllegalArgumentException("Wrong argument type for command AdvanceBy.");
        }
        handleAdvanceBy(by);
      }
      case SAVE_QUIT -> {
        String filename;
        try {
          filename = (String)param.getValue();
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

  // COMMAND HANDLERS //

  /**
   * Internal GET_COMMAND handler.
   */
  private void handleGetCommand() {
    do {
      output.clearScreen();
      output.emitHeader();
      output.emitField();
      output.emitPrompt();
      nextCommand = input.readCommand();
      if (nextCommand == null) {
        output.setError(ErrorMsg.INVALID_COMMAND);
      }
    } while (nextCommand == null);
  }

  /**
   * Internal ENTRY handler.
   */
  private void handleEntry() {
    output.clearScreen();
    output.emitHeader();

    Boolean load;
    do {
      output.emitLoadQuestion();
      load = input.readYesNo();
    } while (load == null);

    if (load) {
      output.emitFilenameQuestion();
      String filename = input.getStringEntered();
      nextCommand = new Command(CommandType.RESUME, new Param<>(filename));
      return;
    }

    nextCommand = new Command(CommandType.SET_STRATEGY);
  }

  /**
   * Internal RESUME handler.
   */
  private void handleResume(String filename) {
    try {
      String fileRead = input.readFile(filename);
      epochs = converter.fromJSON(fileRead);
    } catch (Throwable err) {
      output.setError(ErrorMsg.INVALID_FILE);
      nextCommand = new Command(CommandType.ENTRY);
      return;
    }
    output = new Output(epochs);
    input = new Input(epochs);
    nextCommand = new Command(CommandType.GET_COMMAND);
  }

  /**
   * Internal SET_STRATEGY handler.
   */
  private void handleSetStrategy() {
    StrategyType selectedStrategy;

    do {
      output.emitStrategyQuestion();
      selectedStrategy = input.readStrategy();
      if (selectedStrategy == null) {
        output.setError(ErrorMsg.INVALID_STRATEGY);
      }
    } while (selectedStrategy == null);

    epochs.setStrategyType(selectedStrategy);

    nextCommand = new Command(CommandType.SET_TRAVERSAL);
  }

  /**
   * Internal SET_TRAVERSE handler.
   */
  private void handleSetTraversal() {
    TraverseBase selectedAlgorithm;

    do {
      output.emitTraverseQuestion();
      selectedAlgorithm = input.readTraverse();
      if (selectedAlgorithm == null) {
        output.setError(ErrorMsg.INVALID_TRAVERSE);
      }
    } while (selectedAlgorithm == null);

    epochs.setTraverseAlgorithm(selectedAlgorithm);

    nextCommand = new Command(CommandType.GET_COMMAND);
  }

  /**
   * Internal ADVANCE handler.
   */
  private void handleAdvance() {
    epochs.advance();
    if (epochs.isSimulationFinished()) {
      finalSequence();
      return;
    }

    nextCommand = new Command(CommandType.GET_COMMAND);
  }

  /**
   * Internal ADVANCE_BY handler.
   */
  private void handleAdvanceBy(int by) {
    epochs.advanceBy(by);
    if (epochs.isSimulationFinished()) {
      finalSequence();
      return;
    }

    nextCommand = new Command(CommandType.GET_COMMAND);
  }

  /**
   * Internal SAVE_QUIT handler.
   */
  private void handleSaveQuit(String filename) {
    try {
      String epochsJson = converter.toJSON(epochs);
      output.writeFile(epochsJson, filename);
    } catch (Throwable err) {
      output.setError(ErrorMsg.ERROR_SAVE);
      nextCommand = new Command(CommandType.GET_COMMAND);
      return;
    }
    nextCommand = new Command(CommandType.QUIT);
  }

  /**
   * Internal QUIT handler.
   */
  private void handleQuit() {
    nextCommand = null;
  }

  /**
   * Internal HELP handler.
   */
  private void handleHelp() {
    output.clearScreen();
    output.emitHelpPage();
    input.getStringEntered();
    nextCommand = new Command(CommandType.GET_COMMAND);
  }

  /**
   * Internal final sequence handler.
   */
  private void finalSequence() {
    output.emitFinalStats();

    Boolean save;
    do {
      output.emitFinalSaveQuestion();
      save = input.readYesNo();
    } while (save == null);

    if (save) {
      output.emitFilenameQuestion();
      String filename = input.getStringEntered();
      nextCommand = new Command(CommandType.SAVE_QUIT, new Param<>(filename));
      return;
    }

    nextCommand = new Command(CommandType.QUIT);
  }
}
