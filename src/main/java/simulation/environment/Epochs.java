package simulation.environment;

import java.util.stream.IntStream;
import io.arguments.Difficulty;
import simulation.computation.BuyingAlgorithm;
import simulation.computation.Dice;
import simulation.computation.SellingAlgorithm;
import simulation.computation.TraverseBase;
import simulation.player.PlayerState;
import simulation.player.PlayerStorage;
import simulation.strategy.StrategyType;
import simulation.vilages.Road;
import simulation.vilages.Thugs;
import simulation.vilages.Village;

/**
 * Simulation execution class
 */
public class Epochs {
  private int                 count = 0;
  private BuyingAlgorithm     buyingAlgorithm;
  private SellingAlgorithm    sellingAlgorithm;
  private final PlayerStorage playerStorage;
  private final PlayerState   playerState;
  private StrategyType        strategyType;
  private TraverseBase        traverseAlgorithm;
  private final Dice          dice;
  private final VillageMap    villageMap;
  private boolean             finishTheSimulation;
  private Village             currentVillage;
  private final Difficulty difficulty;

  /**
   * Constructor, construct using setters in controller.
   */
  public Epochs(Difficulty difficulty) {
    this.difficulty = difficulty;
    this.playerStorage       = new PlayerStorage(this);
    this.playerState         = new PlayerState();
    this.dice                = new Dice();
    this.villageMap          = new VillageMap(this);
    this.finishTheSimulation = false;
    currentVillage           = null;
  }

  /**
   * Strategy type setter, init algorithms now, remember to call before
   * advancing an epoch.
   * @param strategyType chosen strategy type.
   */
  public void setStrategyType(StrategyType strategyType) {
    this.strategyType     = strategyType;
    this.buyingAlgorithm  = new BuyingAlgorithm(this);
    this.sellingAlgorithm = new SellingAlgorithm(this);
  }

  /**
   * Traverse algorithm setter, remember to call before advancing an epoch.
   * @param traverseAlgorithm chosen traversal algorithm.
   */
  public void setTraverseAlgorithm(TraverseBase traverseAlgorithm) {
    this.traverseAlgorithm = traverseAlgorithm;
  }

  /**
   * Advance one epoch: Travel, Sell, Buy, Increment count
   */
  public void advance() {
    if (count % 3 == 0) { villageMap.regenerateMap(); }

    if (finishTheSimulation) return;
    travelingSequence();
    if (finishTheSimulation) return;

    sellingSequence();
    buyingSequence();
    ++count;
  }

  /**
   * Advance several epochs.
   * @param count the number of epochs to advance.
   */
  public void advanceBy(int count) {
    IntStream.range(0, count).forEach(i -> advance());
  }

  /**
   * Execute the travelling sequence: consume daily food or die, pay the cost of
   * travel or die, possibly get attacked, set current position and village.
   */
  private void travelingSequence() {
    Village  nextVillage         = traverseAlgorithm.getNext();
    Position nextVillagePosition = nextVillage.getPosition();
    Position playerPosition      = playerState.getCurrentPosition();
    Road     road = new Road(playerPosition, nextVillagePosition, this);

    float   risk              = road.calculateRisk();
    float   distance          = road.calculateDistance();
    float   costPerUnitOfRoad = strategyType.getTravelCost();
    boolean toBeAttacked      = dice.roll(risk);

    playerStorage.consumeDailyFood();

    float travelCost =
      Math.min(distance * costPerUnitOfRoad, playerStorage.getMoney());
    playerStorage.subtractMoney(travelCost);

    if (Float.isNaN(playerStorage.getMoney())) {
      throw new NumberFormatException("NaN");
    }

    if (playerState.isDead()) {
      finishTheSimulation = true;
      return;
    }

    playerState.setAttacked(false);
    if (toBeAttacked) {
      Thugs thugs = new Thugs(this);
      thugs.steal();
      playerState.setAttacked(true);
    }

    playerState.setCurrentPosition(nextVillagePosition);
    currentVillage = nextVillage;
  }

  /**
   * Execute the selling sequence: generate Transactions and execute them
   */
  private void sellingSequence() {
    var transactions = sellingAlgorithm.generateTransactions(currentVillage);
    transactions.forEach(transaction -> {
      boolean villageCanFulfil =
        currentVillage.isTransactionPossible(transaction);
      boolean playerCanFulfil =
        playerStorage.isTransactionPossible(transaction);

      if (villageCanFulfil && playerCanFulfil) {
        transaction.execute(currentVillage);
      }
    });
  }

  /**
   * Execute the buying sequence: generate Transactions and execute them
   */
  private void buyingSequence() {
    var transactions = buyingAlgorithm.generateTransactions(currentVillage);
    transactions.forEach(transaction -> {
      boolean villageCanFulfil =
        currentVillage.isTransactionPossible(transaction);
      boolean playerCanFulfil =
        playerStorage.isTransactionPossible(transaction);

      if (villageCanFulfil && playerCanFulfil) {
        transaction.execute(currentVillage);
      }
    });
  }

  /**
   * StrategyType getter.
   * @return Selected strategy type.
   */
  public StrategyType getStrategyType() {
    return strategyType;
  }

  /**
   * Get elapsed epochs.
   * @return elapsed epochs up to this point.
   */
  public int getCount() {
    return count;
  }

  /**
   * Player storage getter.
   * @return playerStorage
   */
  public PlayerStorage getPlayerStorage() {
    return playerStorage;
  }

  /**
   * PlayerState getter.
   * @return PlayerState.
   */
  public PlayerState getPlayerState() {
    return playerState;
  }

  /**
   * If the simulation should be finished.
   * @return true if simulation finished.
   */
  public boolean isSimulationFinished() {
    return finishTheSimulation;
  }

  /**
   * Village map getter.
   * @return VillageMap
   */
  public VillageMap getVillageMap() {
    return villageMap;
  }

  public BuyingAlgorithm getBuyingAlgorithm() {
    return buyingAlgorithm;
  }

  public SellingAlgorithm getSellingAlgorithm() {
    return sellingAlgorithm;
  }

  public TraverseBase getTraverseAlgorithm() {
    return traverseAlgorithm;
  }

  public Difficulty getDifficulty() {
    return difficulty;
  }
}
