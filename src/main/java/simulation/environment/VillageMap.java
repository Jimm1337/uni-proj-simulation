package simulation.environment;

import io.arguments.Difficulty;
import java.util.ArrayList;
import java.util.stream.IntStream;
import simulation.player.PlayerState;
import simulation.vilages.Road;
import simulation.vilages.Village;

/**
 * Stores and manages simulation's map.
 */
public class VillageMap {
  private static final int   COUNT_OF_VILLAGES = 5;
  private static final float POS_RAND_MULTI    = 20.0f;

  private ArrayList<Village> villages;
  private final Difficulty   difficulty;
  private final Epochs       epochs;

  /**
   * Singleton constructor, initializes map, grabs difficulty instance.
   * @param epochs Epochs.
   */
  public VillageMap(Epochs epochs) {
    this.epochs = epochs;

    difficulty = epochs.getDifficulty();

    regenerateMap();
  }

  /**
   * Randomly generate villages
   */
  public void regenerateMap() {
    villages = new ArrayList<>();
    IntStream.range(0, COUNT_OF_VILLAGES).forEach(i -> {
      float    randomX        = difficulty.getRandomFloat() * POS_RAND_MULTI;
      float    randomY        = difficulty.getRandomFloat() * POS_RAND_MULTI;
      Position randomPosition = new Position(randomX, randomY);
      Village  newVillage     = new Village(randomPosition, epochs);
      villages.add(i, newVillage);
    });
  }

  /**
   * Get a village with the best calculated price index.
   * @return A village with best prices.
   */
  public Village getBestPrices() {
    Village currentBest           = villages.get(0);
    float   currentBestPriceIndex = currentBest.getPriceIndex();

    for (Village village : villages) {
      float thisVillagePriceIndex = village.getPriceIndex();

      if (thisVillagePriceIndex < currentBestPriceIndex) {
        currentBest           = village;
        currentBestPriceIndex = thisVillagePriceIndex;
      }
    }

    return currentBest;
  }

  /**
   * Get a village closest to the player.
   * @return A village closest to the player.
   */
  public Village getClosestToPlayer() {
    final PlayerState playerState    = epochs.getPlayerState();
    final Position    playerPosition = playerState.getCurrentPosition();

    Village  currentBest         = villages.get(0);
    Position currentBestPosition = currentBest.getPosition();
    Road     currentBestRoad =
      new Road(playerPosition, currentBestPosition, epochs);
    float currentBestDistance = currentBestRoad.calculateDistance();

    for (Village village : villages) {
      Position thisVillagePosition = village.getPosition();
      Road     thisVillageRoad =
        new Road(playerPosition, thisVillagePosition, epochs);
      float thisVillageDistance = thisVillageRoad.calculateDistance();

      if (thisVillageDistance < currentBestDistance) {
        currentBest         = village;
        currentBestDistance = thisVillageDistance;
      }
    }

    return currentBest;
  }

  /**
   * Get all villages as an ArrayList
   * @return ArrayList of villages.
   */
  public ArrayList<Village> getVillages() {
    return villages;
  }
}
