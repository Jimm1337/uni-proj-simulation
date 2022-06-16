package io.console;

import com.google.gson.JsonSyntaxException;
import io.arguments.Difficulty;
import io.json.Converter;
import simulation.computation.TraverseBase;
import simulation.computation.TraverseDistance;
import simulation.environment.Epochs;
import simulation.strategy.AggressiveStrategy;
import simulation.strategy.StrategyType;

import java.io.IOException;

/**
 * Controls the program flow, executes commands.
 */
public class Controller {
  private static final int AUTO_INCREMENT_PAUSE_SECONDS = 3;

  private Input                    input;
  private Output                   output;
  private Epochs                   epochs;
  private Command                  nextCommand;
  private Converter converter;

  /**
   * Constructor, creates io, grabs Epochs (main simulation class) instance.
   * @param difficulty Difficulty.
   */
  public Controller(Difficulty difficulty) {
    this.epochs       = new Epochs(difficulty);
    this.input        = new Input(epochs);
    this.output       = new Output(epochs);
    this.nextCommand  = new Command(CommandType.ENTRY);
    this.converter = new Converter();
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

    //debug entry
//    epochs.setStrategyType(new AggressiveStrategy(epochs));
//    epochs.setTraverseAlgorithm(new TraverseDistance(epochs));
//    while (true) epochs.advance();
  }

  private void handleResume(String filename) {
    try {
      epochs = converter.fromJSON(filename);
    } catch (JsonSyntaxException err) {
      System.out.println("Invalid file. Try again.");
      nextCommand = new Command(CommandType.RESUME, new Param<>(filename));
      return;
    }
    output = new Output(epochs);
    input = new Input(epochs);
    nextCommand = new Command(CommandType.GET_COMMAND);
  }

  private void handleSetStrategy() {
    StrategyType selectedStrategy;

    do {
      output.emitStrategyQuestion();
      selectedStrategy = input.readStrategy();
    } while (selectedStrategy == null);

    epochs.setStrategyType(selectedStrategy);

    nextCommand = new Command(CommandType.SET_TRAVERSAL);
  }

  private void handleSetTraversal() {
    TraverseBase selectedAlgorithm;

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
    String epochsJson = converter.toJSON(epochs);
    try {
      output.writeFile(epochsJson, filename);
    } catch (IOException e) {
      System.out.println("Problems saving to a file. Try again.");
      nextCommand = new Command(CommandType.SAVE_QUIT, new Param<>(filename));
      return;
    }
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

    Boolean save;
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
