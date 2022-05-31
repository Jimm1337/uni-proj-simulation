package simulation.player;

import simulation.environment.Position;

/**
 * Holds the information about position and whether an attack happened last
 * epoch.
 */
public class PlayerState {
  private static PlayerState instance;
  private boolean            isAttacked;
  private Position           currentPosition;

  /**
   * Singleton constructor.
   */
  private PlayerState() {
    isAttacked      = false;
    currentPosition = new Position(0, 0);
  }

  /**
   * Singleton instance getter. Creates instance if it doesn't yet exist.
   * @return The only instance of PlayerState
   */
  public static PlayerState getInstance() {
    if (instance == null) { instance = new PlayerState(); }
    return instance;
  }

  /**
   * Gets the attacked state.
   * @return Whether an attack happened last epoch.
   */
  public boolean isAttacked() {
    return isAttacked;
  }

  /**
   * Gets the current position.
   * @return The current position.
   */
  public Position getCurrentPosition() {
    return currentPosition;
  }

  /**
   * Sets the last attacked info.
   * @param attacked Whether an attack happened last epoch.
   */
  public void setAttacked(boolean attacked) {
    isAttacked = attacked;
  }

  /**
   * Sets new position of the player.
   * @param newPosition The position to be set.
   */
  public void setCurrentPosition(Position newPosition) {
    currentPosition = newPosition;
  }
}
