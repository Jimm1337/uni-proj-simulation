package simulation.player;

import simulation.environment.Position;

/**
 * Holds the information about position and whether an attack happened last
 * epoch.
 */
public class PlayerState {
  private boolean            isAttacked;
  private Position           currentPosition;
  private boolean            isDead;

  /**
   * Constructor.
   */
  public PlayerState() {
    isAttacked      = false;
    isDead          = false;
    currentPosition = new Position(0, 0);
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

  /**
   * Finish the simulation
   */
  public void die() {
    isDead = true;
  }

  /**
   * Check if dead.
   * @return true if dead.
   */
  public boolean isDead() {
    return isDead;
  }
}
