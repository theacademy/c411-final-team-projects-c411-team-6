package org.mthree.dao;

import org.mthree.dto.Item;
import org.mthree.dto.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionDaoImpl implements TransactionDao {
    private final JdbcTemplate jdbcTemplate;

    public TransactionDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTransaction(Transaction txn){
        String sql = "INSERT IGNORE INTO items(plaid_transaction_id, category, type, name, amount, date) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, txn.getId(), txn.getCategory(), txn.getType(), txn.getName(), txn.getAmount(), txn.getDate());
    }
}
