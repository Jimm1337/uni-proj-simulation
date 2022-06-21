package simulation.environment;

/**
 * Represents a position on a plane.
 */
public class Position {
  private final float x;
  private final float y;

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
}
