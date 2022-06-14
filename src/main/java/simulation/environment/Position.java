package simulation.environment;

/**
 * Represents a position on a plane.
 */
public class Position {
  private float x;
  private float y;

  /**
   * Position of constructor.
   * @param x X coordinate.
   * @param y Y coordinate.
   */
  public Position(float x, float y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Gets the X coordinate.
   * @return The X coordinate.
   */
  public float getX() {
    return x;
  }

  /**
   * Gets the Y coordinate.
   * @return The Y coordinate.
   */
  public float getY() {
    return y;
  }

  /**
   * Sets the X coordinate.
   * @param x New X coordinate.
   */
  public void setX(float x) {
    this.x = x;
  }

  /**
   * Sets the Y coordinate.
   * @param y New Y coordinate.
   */
  public void setY(float y) {
    this.y = y;
  }
}
