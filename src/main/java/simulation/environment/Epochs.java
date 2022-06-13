package simulation.environment;

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

import java.util.stream.IntStream;

/**
 * Simulation execution class
 */
public class Epochs {
  private int count = 0;
  private final BuyingAlgorithm buyingAlgorithm;
  private final SellingAlgorithm sellingAlgorithm;
  private final PlayerStorage playerStorage;
  private final PlayerState playerState;
  private final StrategyType strategyType;
  private final TraverseBase traverseAlgorithm;
  private final Dice dice;
  private boolean finishTheSimulation;
  private Village currentVillage;

  static private Epochs instance;

  /**
   * Constructor, construct using the init method instead.
   * @param buyingAlgorithm buying algorithm class.
   * @param sellingAlgorithm selling algorithm class.
   * @param strategyType strategy type.
   * @param traverseAlgorithm traversal algorithm
   */
  private Epochs(BuyingAlgorithm buyingAlgorithm, SellingAlgorithm sellingAlgorithm, StrategyType strategyType, TraverseBase traverseAlgorithm) {
    this.buyingAlgorithm = buyingAlgorithm;
    this.sellingAlgorithm = sellingAlgorithm;
    this.playerStorage = PlayerStorage.getInstance();
    this.playerState = PlayerState.getInstance();
    this.strategyType = strategyType;
    this.traverseAlgorithm = traverseAlgorithm;
    this.dice = new Dice();
    this.finishTheSimulation = false;
    currentVillage = null;
  }

  /**
   * Init method used either when loading or creating a new simulation.
   * @param buyingAlgorithm buying algorithm class.
   * @param sellingAlgorithm selling algorithm class.
   * @param strategyType strategy type.
   * @param traverseAlgorithm traversal algorithm
   */
  public static void init(BuyingAlgorithm buyingAlgorithm, SellingAlgorithm sellingAlgorithm, StrategyType strategyType, TraverseBase traverseAlgorithm) {
    if (instance == null) {
      instance = new Epochs(buyingAlgorithm, sellingAlgorithm, strategyType, traverseAlgorithm);
    }
  }

  /**
   * Instance grabber.
   * @return The only instance.
   */
  public static Epochs getInstance() {
    if (instance == null) {
      throw new RuntimeException("Failed to init Epochs");
    }

    return instance;
  }

  /**
   * Advance one epoch: Travel, Sell, Buy, Increment count
   */
  public void advance() {
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
   * Execute the travelling sequence: consume daily food or die, pay the cost of travel, possibly get attacked, set current position and village.
   */
  private void travelingSequence() {
    Village nextVillage = traverseAlgorithm.getNext();
    Position nextVillagePosition = nextVillage.getPosition();
    Position playerPosition = playerState.getCurrentPosition();
    Road road = new Road(playerPosition, nextVillagePosition);

    float risk = road.calculateRisk();
    float distance = road.calculateDistance();
    float costPerUnitOfRoad = strategyType.getTravelCost();
    boolean toBeAttacked = dice.roll(risk);

    playerStorage.consumeDailyFood();
    if (playerState.isDead()) {
      finishTheSimulation = true;
      return;
    }

    float travelCost = distance * costPerUnitOfRoad;
    playerStorage.subtractMoney(travelCost);

    playerState.setAttacked(false);
    if (toBeAttacked) {
      Thugs thugs = new Thugs();
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
    var transactions = sellingAlgorithm.generateTransactions();
    transactions.forEach(transaction -> transaction.execute(currentVillage));
  }

  /**
   * Execute the buying sequence: generate Transactions and execute them
   */
  private void buyingSequence () {
    var transactions = buyingAlgorithm.generateTransactions();
    transactions.forEach(transaction -> transaction.execute(currentVillage));
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
}
