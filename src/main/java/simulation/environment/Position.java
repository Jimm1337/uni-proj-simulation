package simulation.environment;

/**
 * Represents a position on a plane.
 */
public class Position {
  private int x;
  private int y;

  /**
   * Position of constructor.
   * @param x X coordinate.
   * @param y Y coordinate.
   */
  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Gets the X coordinate.
   * @return The X coordinate.
   */
  public int getX() {
    return x;
  }

  /**
   * Gets the Y coordinate.
   * @return The Y coordinate.
   */
  public int getY() {
    return y;
  }

  /**
   * Sets the X coordinate.
   * @param x New X coordinate.
   */
  public void setX(int x) {
    this.x = x;
  }

  /**
   * Sets the Y coordinate.
   * @param y New Y coordinate.
   */
  public void setY(int y) {
    this.y = y;
  }
}
