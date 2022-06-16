package io.console;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;
import simulation.environment.Epochs;
import simulation.environment.Position;
import simulation.environment.VillageMap;
import simulation.goods.Product;
import simulation.goods.ProductType;
import simulation.player.PlayerState;
import simulation.player.PlayerStorage;
import simulation.strategy.StrategyType;
import simulation.vilages.Village;

/**
 * Controls the console output.
 */
public class Output {
  private static final String PROMPT_TEXT = "Command (h<Enter> for help): ";
  private static final String INVALID_COMMAND_TEXT =
    "Invalid command-argument combination!";
  private static final String VILLAGE_SPACING  = "25";
  private static final String HEADER           = "~~~ Merchant Simulation ~~~";
  private static final String LOAD_MESSAGE     = "Load?[y/n]: ";
  private static final String FILENAME_MESSAGE = "Filename: ";
  private static final String INVALID_FILE_MESSAGE = "Invalid file!";
  private static final String STRATEGY_MESSAGE =
    "Strategy Type?[a (aggressive)/ b (balanced)/ c (conservative)]: ";
  private static final String INVALID_STRATEGY_MESSAGE = "Invalid strategy!";
  private static final String TRAVERSE_MESSAGE =
    "Traverse method?[p (price)/ d (distance)]: ";
  private static final String INVALID_TRAVERSE_MESSAGE =
    "Invalid traverse method!";
  private static final String PRESS_TO_STOP_MESSAGE = "Press 's' to stop.";
  private static final String FINAL_SAVE_QUESTION_MESSAGE = "Save?[y/n]: ";
  private static final String TRY_AGAIN_MESSAGE           = "Try again?[y/n]: ";

  private final Epochs epochs;
  private String       cachedHelp;

  /**
   * Grabs epochs, prepares cachedHelp for lazy evaluation.
   * @param epochs Epochs.
   */
  public Output(Epochs epochs) {
    this.epochs     = epochs;
    this.cachedHelp = null;
  }

  /**
   * Clears screen.
   */
  public void clearScreen() {
    try {
      Runtime.getRuntime().exec("clear");
    } catch (Throwable ignored) {}

    System.out.flush();
  }

  /**
   * Emit header.
   */
  public void emitHeader() {
    draw(HEADER);
  }

  /**
   * Ask for load.
   */
  public void emitLoadQuestion() {
    drawPromptLike(LOAD_MESSAGE);
  }

  /**
   * Ask for filename.
   */
  public void emitFilenameQuestion() {
    drawPromptLike(FILENAME_MESSAGE);
  }

  /**
   * Signal invalid file.
   */
  public void emitInvalidFileMessage() {
    draw(INVALID_FILE_MESSAGE);
  }

  /**
   * Ask for strategy type.
   */
  public void emitStrategyQuestion() {
    drawPromptLike(STRATEGY_MESSAGE);
  }

  /**
   * Signal invalid strategy type.
   */
  public void emitInvalidStrategy() {
    draw(INVALID_STRATEGY_MESSAGE);
  }

  /**
   * Ask for traverse strategy.
   */
  public void emitTraverseQuestion() {
    drawPromptLike(TRAVERSE_MESSAGE);
  }

  /**
   * Signal invalid traverse strategy.
   */
  public void emitInvalidTraverse() {
    draw(INVALID_TRAVERSE_MESSAGE);
  }

  /**
   * Print a hint to stop Advance auto.
   */
  public void emitStopHint() {
    drawPromptLike(PRESS_TO_STOP_MESSAGE);
  }

  /**
   * Ask for another try.
   */
  public void emitTryAgain() {
    drawPromptLike(TRY_AGAIN_MESSAGE);
  }

  /**
   * Ask if the simulation result should be saved after it has finished.
   */
  public void emitFinalSaveQuestion() {
    drawPromptLike(FINAL_SAVE_QUESTION_MESSAGE);
  }

  /**
   * Print help page.
   */
  public void emitHelpPage() {
    if (cachedHelp == null) {
      StringBuilder builder = new StringBuilder();

      builder.append(
        "(Command [Optional - without :]:PARAM [Optional]<Enter>) |Command Name| - Description.\n");
      for (CommandType type : CommandType.values()) {
        builder.append(type.manualEntry());
      }
      builder.append("Press Enter to close this manual.");

      cachedHelp = builder.toString();
    }

    draw(cachedHelp);
  }

  /**
   * Print final message.
   */
  public void emitFinalStats() {
    String message = "Simulation finished!\n"
                     + "Epochs count: " + epochs.getCount() + '\n';

    draw(message);
  }

  /**
   * Print current villages, player, and info.
   */
  public void emitField() {
    String generalInfo            = generateGeneralInfoRepresentation();
    String villagesRepresentation = generateVillagesRepresentation();
    String playerRepresentation   = generatePlayerRepresentation();

    draw(generalInfo);
    draw(villagesRepresentation);
    draw(playerRepresentation);
  }

  /**
   * Print Command prompt.
   */
  public void emitPrompt() {
    drawPromptLike(PROMPT_TEXT);
  }

  /**
   * Signal Invalid command.
   */
  public void emitInvalidCommand() {
    draw(INVALID_COMMAND_TEXT);
  }

  /**
   * Internal drawing implementation.
   * @param toDraw String to draw.
   */
  private void draw(String toDraw) {
    System.out.println(toDraw);
  }

  /**
   * Internal prompt drawing implementation.
   * @param toDraw Prompt like representation to draw.
   */
  private void drawPromptLike(String toDraw) {
    System.out.print(toDraw);
    System.out.flush();
  }

  /**
   * Internal info representation generator.
   * @return Info.
   */
  private String generateGeneralInfoRepresentation() {
    int    currentEpoch = epochs.getCount();
    String info         = String.format("Epoch: %d", currentEpoch);

    return info;
  }

  /**
   * Internal villages representation generator.
   * @return Villages representation.
   */
  private String generateVillagesRepresentation() {
    VillageMap    map      = epochs.getVillageMap();
    List<Village> villages = map.getVillages();

    StringBuilder rowNames        = new StringBuilder();
    StringBuilder rowPositions    = new StringBuilder();
    StringBuilder rowPriceIndex   = new StringBuilder();
    StringBuilder rowVillageMoney = new StringBuilder();

    IntStream.range(0, villages.size()).forEachOrdered(i -> {
      Village currentVillage = villages.get(i);

      String villageNameStr = String.format(
        "|%" + VILLAGE_SPACING + "s", String.format("Village no. %d", i));
      rowNames.append(villageNameStr);

      Position villagePosition    = currentVillage.getPosition();
      String   villagePositionStr = String.format(
        "|%" + VILLAGE_SPACING + "s",
        String.format(
          "X: %.2f, Y: %.2f", villagePosition.getX(), villagePosition.getY()));
      rowPositions.append(villagePositionStr);

      float  villagePriceIndex    = currentVillage.getPriceIndex();
      String villagePriceIndexStr = String.format(
        "|%" + VILLAGE_SPACING + "s",
        String.format("Price Index: %.2f", villagePriceIndex));
      rowPriceIndex.append(villagePriceIndexStr);

      float  villageMoney    = currentVillage.getMoney();
      String villageMoneyStr = String.format(
        "|%" + VILLAGE_SPACING + "s",
        String.format("Vault: %.2f", villageMoney));
      rowVillageMoney.append(villageMoneyStr);
    });
    rowNames.append("|\n");
    rowPositions.append("|\n");
    rowVillageMoney.append("|\n");
    rowPriceIndex.append("|\n");

    int    oneLineLength = rowNames.length();
    String spacer        = "-".repeat(oneLineLength - 1) + '\n';

    StringBuilder villagesBuilder = new StringBuilder();
    villagesBuilder.append(spacer);
    villagesBuilder.append(rowNames);
    villagesBuilder.append(spacer);
    villagesBuilder.append(rowPositions);
    villagesBuilder.append(rowPriceIndex);
    villagesBuilder.append(rowVillageMoney);
    villagesBuilder.append(spacer);

    String villagesRepresentation = villagesBuilder.toString();

    return villagesRepresentation;
  }

  /**
   * Internal player representation generator.
   * @return Player representation.
   */
  private String generatePlayerRepresentation() {
    PlayerStorage storage  = epochs.getPlayerStorage();
    PlayerState   state    = epochs.getPlayerState();
    StrategyType  strategy = epochs.getStrategyType();

    StringBuilder playerBuilder = new StringBuilder();
    playerBuilder.append("Merchant's status:\n");

    Position position    = state.getCurrentPosition();
    String   rowPosition = String.format(
      "Position -> X: %.2f, Y: %.2f\n", position.getX(), position.getY());
    playerBuilder.append(rowPosition);

    Product foodStock        = storage.getProduct(ProductType.FOOD);
    float   dailyConsumption = strategy.getFoodConsumption();
    String  rowFood          = String.format(
      "Food -> Stock: %.2f, Consumption: %.2f\n",
      foodStock.getWeight(),
      dailyConsumption);
    playerBuilder.append(rowFood);

    float  money    = storage.getMoney();
    String rowMoney = String.format("Money -> %.2f\n", money);
    playerBuilder.append(rowMoney);

    boolean lastEpochAttacked = state.isAttacked();
    String  rowAttacked =
      "Was Attacked last epoch? -> " + lastEpochAttacked + '\n';
    playerBuilder.append(rowAttacked);

    StringBuilder stockBuilder = new StringBuilder();
    stockBuilder.append("Stock:\n");

    Product gems     = storage.getProduct(ProductType.GEM);
    Product spice    = storage.getProduct(ProductType.SPICE);
    Product soap     = storage.getProduct(ProductType.SOAP);
    String  gemRow   = String.format("  .Gem   -> %.2f\n", gems.getWeight());
    String  spiceRow = String.format("  .Spice -> %.2f\n", spice.getWeight());
    String  soapRow  = String.format("  .Soap  -> %.2f\n", soap.getWeight());
    stockBuilder.append(gemRow);
    stockBuilder.append(spiceRow);
    stockBuilder.append(soapRow);
    playerBuilder.append(stockBuilder);

    String playerRepresentation = playerBuilder.toString();

    return playerRepresentation;
  }

  /**
   * Write to a file.
   * @param contents Contents to be written.
   * @param filename Filename to be written.
   * @throws IOException Problems writing to a file.
   */
  public void writeFile(String contents, String filename) throws IOException {
    Path path = Paths.get(filename);
    byte[] strToBytes = contents.getBytes();
    Files.write(path, strToBytes);
  }

  /**
   * Read from a file.
   * @param filename Filename to be read.
   * @return Contents.
   * @throws IOException Problems reading a file.
   */
  public String readFile(String filename) throws IOException {
    Path path = Paths.get(filename);
    byte[] read = Files.readAllBytes(path);
    return new String(read);
  }
}
