package io.console;

import io.arguments.Difficulty;
import simulation.computation.TraverseBase;
import simulation.environment.Epochs;
import simulation.strategy.StrategyType;

/**
 * Controls the program flow, executes commands.
 */
public class Controller {
  private static final int AUTO_INCREMENT_PAUSE_SECONDS = 3;

  private Input                    input;
  private Output                   output;
  private final Epochs                   epochs;
  private Command                  nextCommand;

  /**
   * Constructor, creates io, grabs Epochs (main simulation class) instance.
   * @param difficulty Difficulty.
   */
  public Controller(Difficulty difficulty) {
    this.epochs       = new Epochs(difficulty);
    this.input        = new Input(epochs);
    this.output       = new Output(epochs);
    this.nextCommand  = new Command(CommandType.ENTRY);
  }

  /**
   * Start controlling the simulation. Spin while nextCommand isn't set to null.
   */
  public void entry() {
    while (nextCommand != null) {
      try {
        executeCommand(nextCommand);
      } catch (IllegalArgumentException err) {
        output.emitInvalidCommand();
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

  // COMMAND HANDLERS // //todo

  private void handleGetCommand() {
    do {
      output.clearScreen();
      output.emitHeader();
      output.emitField();
      if (nextCommand == null) {
        output.emitInvalidCommand();
      }
      output.emitPrompt();
      nextCommand = input.readCommand();
    } while (nextCommand == null);
  }

  private void handleEntry() {
    output.clearScreen();
    output.emitHeader();
    output.emitLoadQuestion();
    boolean load = input.readYesNo();
    if (load) {
      output.emitFilenameQuestion();
      String filename = input.getStringEntered();
      nextCommand = new Command(CommandType.RESUME, new Param<>(filename));
      return;
    }

    nextCommand = new Command(CommandType.SET_STRATEGY);

    //debug entry
//    epochs.setStrategyType(new AggressiveStrategy());
//    epochs.setTraverseAlgorithm(new TraverseDistance());
//    while (true) epochs.advance();
  }

  private void handleResume(String filename) {
    System.out.println("Unimplemented.");
    nextCommand = new Command(CommandType.SET_STRATEGY);
    //todo: load json
    output = new Output(epochs);
    input = new Input(epochs);
  }

  private void handleSetStrategy() {
    StrategyType selectedStrategy = null;

    do {
      output.emitStrategyQuestion();
      selectedStrategy = input.readStrategy();
    } while (selectedStrategy == null);

    epochs.setStrategyType(selectedStrategy);

    nextCommand = new Command(CommandType.SET_TRAVERSAL);
  }

  private void handleSetTraversal() {
    TraverseBase selectedAlgorithm = null;

    do {
      output.emitTraverseQuestion();
      selectedAlgorithm = input.readTraverse();
    } while (selectedAlgorithm == null);

    epochs.setTraverseAlgorithm(selectedAlgorithm);

    nextCommand = new Command(CommandType.GET_COMMAND);
  }

  private void handleAdvance() {
    epochs.advance();
    if (epochs.isSimulationFinished()) {
      finalSequence();
      return;
    }

    nextCommand = new Command(CommandType.GET_COMMAND);
  }

  private void handleAdvanceBy(int by) {
    epochs.advanceBy(by);
    if (epochs.isSimulationFinished()) {
      finalSequence();
      return;
    }

    nextCommand = new Command(CommandType.GET_COMMAND);
  }

  private void handleSaveQuit(String filename) {
    System.out.println("Unimplemented.");
    //todo: write json

    nextCommand = new Command(CommandType.QUIT);
  }

  private void handleQuit() {
    nextCommand = null;
  }

  private void handleHelp() {
    output.clearScreen();
    output.emitHelpPage();
    input.getStringEntered();
    nextCommand = new Command(CommandType.GET_COMMAND);
  }

  private void finalSequence() {
    output.emitFinalStats();

    Boolean save = null;
    do {
      output.emitFinalSaveQuestion();
      save = input.readYesNo();
    } while (save == null);

    if (save) {
      String filename = input.getStringEntered();
      nextCommand = new Command(CommandType.SAVE_QUIT, new Param<>(filename));
      return;
    }

    nextCommand = new Command(CommandType.QUIT);
  }
}
