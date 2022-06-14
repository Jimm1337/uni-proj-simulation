package io.console;

import simulation.computation.TraverseBase;
import simulation.environment.Epochs;
import simulation.strategy.StrategyType;

import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Controls the program flow, executes commands.
 */
public class Controller {
  private static final int AUTO_INCREMENT_PAUSE_SECONDS = 3;

  Input input;
  Output output;
  Epochs epochs;
  Command nextCommand;
  ScheduledExecutorService autoExecutor;

  /**
   * Constructor, creates io, grabs Epochs (main simulation class) instance.
   */
  public Controller() {
    this.input = new Input();
    this.output = new Output();
    this.epochs = Epochs.getInstance();
    this.nextCommand = new Command(CommandType.ENTRY);
    this.autoExecutor = null;
  }

  /**
   * Start controlling the simulation. Spin while nextCommand isn't set to null.
   */
  public void entry() {
    while (nextCommand != null) {
      executeCommand(nextCommand);
    }
  }

  /**
   * Execute a given command.
   * @param command The command to be executed.
   * @throws IllegalArgumentException When the param doesn't is of invalid type for given command type.
   */
  private void executeCommand(Command command) {
    CommandType commandType = command.getCommandType();
    Param<?> param = command.getParam();

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
      case ADVANCE_AUTO -> {
        handleAdvanceAuto();
      }
      case ADVANCE_STOP -> {
        handleAdvanceStop();
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
  }

  private void handleResume(String filename) {
    //todo: load json
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

  private void handleAdvanceAuto() {
    do {
      epochs.advance();
      if (epochs.isSimulationFinished()) {
        finalSequence();
        return;
      }
      output.emitField();
      output.emitStopHint();
    } while (!input.isCurrentlyPressed(KeyEvent.VK_S));

    nextCommand = new Command(CommandType.ADVANCE_STOP);
  }

  private void handleAdvanceStop() {
    nextCommand = new Command(CommandType.GET_COMMAND);
  }

  private void handleSaveQuit(String filename) {
    //todo: write json

    nextCommand = null;
  }

  private void handleQuit() {
    nextCommand = null;
  }

  private void handleHelp() {
    output.clearScreen();
    output.emitHelpPage();
    while(!input.isCurrentlyPressed(KeyEvent.VK_ENTER));
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
      //todo: json save

      nextCommand = new Command(CommandType.SAVE_QUIT, new Param<>(filename));
      return;
    }

    nextCommand = new Command(CommandType.QUIT);
  }
}
