package io.console;

/**
 * Simple generic to represent a param.
 * @param <T> Type of param.
 */
record Param<T>(T value) {
  /**
   * Value getter.
   * @return Value of param.
   */
  T getValue() {
    return value;
  }
}
