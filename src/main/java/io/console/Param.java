package io.console;

/**
 * Simple generic to represent a param.
 * @param <T> Type of param.
 */
record Param<T>(T value) {
  T getValue() {
    return value;
  }
}
