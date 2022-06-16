package simulation.vilages;

import io.arguments.Difficulty;
import simulation.environment.Position;
import simulation.player.PlayerState;

/**
 * Road class represents a path between two positions, used for calculations.
 */
public class Road {
  private static final float RISK_RAND_MULTI = 0.002f;

  private final Position start;
  private final Position finish;
  private Difficulty     difficulty;

  /**
   * Default generated constructor.
   * @param start Starting position.
   * @param finish Final position.
   */
  public Road(Position start, Position finish) {
    this.start      = start;
    this.finish     = finish;
    this.difficulty = Difficulty.getInstance();
  }

  /**
   * Calculates the distance between starting and final position.
   * @return Distance between the positions.
   */
  public float calculateDistance() {
    return (float)Math.sqrt(
      Math.pow(start.getX() - finish.getX(), 2) +
      Math.pow(start.getY() - finish.getY(), 2));
  }

  /**
   * Calculates the risk of theft while travelling.
   * @return Risk of theft on this road.
   */
  public float calculateRisk() {
    final float distance = calculateDistance();
    final float riskPerUnit =
      Math.abs(difficulty.getRandomFloat() * RISK_RAND_MULTI);

    return distance * riskPerUnit;
  }
}
