package simulation.goods;

/**
 * Provides an interface for classes capable of making transactions.
 */
public interface TransactionChecker {

  /**
   * Check if transaction is possible (enough money or stock).
   * @param transaction transaction to check.
   * @return true if transaction is valid.
   */
  boolean isTransactionPossible(Transaction transaction);
}
