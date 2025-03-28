package org.mthree.dao;

import org.mthree.dto.Transaction;
import java.util.List;

public interface TransactionDao {
    void saveTransaction(Transaction transaction);
    List<Transaction> getTransactionsByUserId(String userId, int months); // Changed Long to String
}