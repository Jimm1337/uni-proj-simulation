package simulation.environment;

import simulation.computation.BuyingAlgorithm;
import simulation.computation.SellingAlgorithm;
import simulation.computation.TraverseBase;
import simulation.player.PlayerState;
import simulation.player.PlayerStorage;
import simulation.strategy.StrategyType;

import java.util.stream.IntStream;

/**
 * Simulation execution class
 */
public class Epochs {
  private int count = 0;
  private BuyingAlgorithm buyingAlgorithm;
  private SellingAlgorithm sellingAlgorithm;
  private PlayerStorage playerStorage;
  private PlayerState playerState;
  private VillageMap villageMap;
  private StrategyType strategyType;
  private TraverseBase traverseAlgorithm;

  static private Epochs instance;

  /**
   * Constructor, construct using the init method instead.
   * @param buyingAlgorithm buying algorithm class.
   * @param sellingAlgorithm selling algorithm class.
   * @param playerStorage player storage.
   * @param playerState player state.
   * @param villageMap village map.
   * @param strategyType strategy type.
   * @param traverseAlgorithm traversal algorithm
   */
  private Epochs(BuyingAlgorithm buyingAlgorithm, SellingAlgorithm sellingAlgorithm, PlayerStorage playerStorage, PlayerState playerState, VillageMap villageMap, StrategyType strategyType, TraverseBase traverseAlgorithm) {
    this.buyingAlgorithm = buyingAlgorithm;
    this.sellingAlgorithm = sellingAlgorithm;
    this.playerStorage = playerStorage;
    this.playerState = playerState;
    this.villageMap = villageMap;
    this.strategyType = strategyType;
    this.traverseAlgorithm = traverseAlgorithm;
  }

  /**
   * Init method used either when loading or creating a new simulation.
   * @param buyingAlgorithm buying algorithm class.
   * @param sellingAlgorithm selling algorithm class.
   * @param playerStorage player storage.
   * @param playerState player state.
   * @param villageMap village map.
   * @param strategyType strategy type.
   * @param traverseAlgorithm traversal algorithm
   */
  public void init(BuyingAlgorithm buyingAlgorithm, SellingAlgorithm sellingAlgorithm, PlayerStorage playerStorage, PlayerState playerState, VillageMap villageMap, StrategyType strategyType, TraverseBase traverseAlgorithm) {
    if (instance == null) {
      instance = new Epochs(buyingAlgorithm, sellingAlgorithm, playerStorage, playerState, villageMap, strategyType, traverseAlgorithm);
    }
  }

  /**
   * Instance grabber.
   * @return The only instance.
   */
  public Epochs getInstance() {
    if (instance == null) {
      throw new RuntimeException("Failed to init Epochs");
    }

    return instance;
  }

  /**
   * Advance one epoch
   */
  public void advance() {
    //todo
  }

  /**
   * Advance several epochs.
   * @param count the number of epochs to advance.
   */
  public void advanceBy(int count) {
    IntStream.range(0, count).forEach(i -> advance());
  }
}
