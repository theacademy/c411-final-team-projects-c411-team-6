package org.mthree.dao;

import org.mthree.dto.Transaction;

public interface TransactionDao {
    void createTransaction(Transaction txn);
}
