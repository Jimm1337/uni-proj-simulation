package simulation.environment;

import io.arguments.Difficulty;
import simulation.player.PlayerState;
import simulation.vilages.Road;
import simulation.vilages.Village;
import java.util.ArrayList;
import java.util.stream.IntStream;

/**
 * Stores and manages simulation's map.
 */
public class VillageMap {
  private static final int COUNT_OF_VILLAGES = 5;
  private static final int POS_RAND_MULTI = 20;

  ArrayList<Village> villages;
  Difficulty difficulty;
  static VillageMap instance;

  /**
   * Singleton constructor, initializes map, grabs difficulty instance
   */
  private VillageMap() {
    villages = new ArrayList<>(COUNT_OF_VILLAGES);
    difficulty = Difficulty.getInstance();

    regenerateMap();
  }

  /**
   * Instance grabber.
   * @return The only instance of this class.
   */
  static public VillageMap getInstance() {
    if (instance == null) {
      instance = new VillageMap();
    }

    return instance;
  }

  /**
   * Randomly generate villages
   */
  public void regenerateMap() {
    IntStream.range(0, COUNT_OF_VILLAGES).forEach(i -> {
      int randomX = difficulty.getRandomInt() * POS_RAND_MULTI;
      int randomY = difficulty.getRandomInt() * POS_RAND_MULTI;
      Position randomPosition = new Position(randomX, randomY);
      Village newVillage = new Village(randomPosition);
      villages.set(i, newVillage);
    });
  }

  /**
   * Get a village with the best calculated price index.
   * @return A village with best prices.
   */
  public Village getBestPrices() {
    Village currentBest = villages.get(0);
    float currentBestPriceIndex = currentBest.getPriceIndex();

    for (Village village : villages) {
      float thisVillagePriceIndex = village.getPriceIndex();

      if (thisVillagePriceIndex < currentBestPriceIndex) {
        currentBest = village;
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
    final PlayerState playerState = PlayerState.getInstance();
    final Position playerPosition = playerState.getCurrentPosition();

    Village currentBest = villages.get(0);
    Position currentBestPosition = currentBest.getPosition();
    Road currentBestRoad = new Road(playerPosition, currentBestPosition);
    float currentBestDistance = currentBestRoad.calculateDistance();

    for (Village village : villages) {
      Position thisVillagePosition = village.getPosition();
      Road thisVillageRoad = new Road(playerPosition, thisVillagePosition);
      float thisVillageDistance = thisVillageRoad.calculateDistance();

      if (thisVillageDistance < currentBestDistance) {
        currentBest = village;
        currentBestPosition = thisVillagePosition;
        currentBestRoad = thisVillageRoad;
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
