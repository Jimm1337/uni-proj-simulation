package simulation.goods;

/**
 * Defines the internal representation of products.
 */
public class Product {
  private final ProductType type;
  private float             weight;

  /**
   * Product constructor, defines final type and initial weight
   * @param type Product Type as per ProductType enum.
   * @param weight Initial weight in units.
   */
  public Product(ProductType type, float weight) {
    this.type   = type;
    this.weight = weight;
  }

  /**
   * Gets the product type.
   * @return Product type.
   */
  public ProductType getType() {
    return type;
  }

  /**
   * Gets the current weight in weight units.
   * @return Current weight in weight units.
   */
  public float getWeight() {
    return weight;
  }

  /**
   * Adds to the current weight.
   * @param weight Weight to be added.
   */
  public void addWeight(float weight) {
    this.weight += weight;
  }

  /**
   * Tries to subtract from the current weight.
   * It is advised to check whether the subtracted weight is less than or equal
   * the current weight.
   * @param weight Weight to be subtracted.
   * @throws IllegalArgumentException When the subtracted weight is greater than
   *   the current weight.
   */
  public void subtractWeight(float weight) {
    if (this.weight < weight) {
      throw new IllegalArgumentException(
        "Weight after the transaction cannot be negative");
    }
    this.weight -= weight;
  }

  /**
   * Override equals to only include Type.
   * @param o Object to compare.
   * @return True if types are the same.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Product product = (Product)o;

    return type == product.type;
  }

  /**
   * Override hashCode to take into account only the product type.
   * @return .hashCode() of the ProductType.
   */
  @Override
  public int hashCode() {
    return type.hashCode();
  }
}
