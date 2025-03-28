package org.mthree.dao;

import org.mthree.dto.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class TransactionDaoImpl implements TransactionDao {

    private final JdbcTemplate jdbcTemplate;

    public TransactionDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (plaid_transaction_id, account_id, category, type, name, amount, date, account_owner) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE amount = VALUES(amount)";
        jdbcTemplate.update(sql,
                transaction.getPlaidTransactionId(),
                null,
                transaction.getCategory(),
                transaction.getType(),
                transaction.getName(),
                transaction.getAmount(),
                transaction.getDate(),
                transaction.getAccountOwner()
        );
    }

    @Override
    public List<Transaction> getTransactionsByUserId(String userId, int months) {
        String sql = "SELECT * FROM transactions WHERE account_owner = ? AND date >= ?";
        LocalDate startDate = LocalDate.now().minusMonths(months);
        return jdbcTemplate.query(sql, new TransactionRowMapper(), userId, startDate);
    }

    private static class TransactionRowMapper implements RowMapper<Transaction> {
        @Override
        public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            Transaction transaction = new Transaction();
            transaction.setPlaidTransactionId(rs.getString("plaid_transaction_id"));
            transaction.setCategory(rs.getString("category"));
            transaction.setType(rs.getString("type"));
            transaction.setName(rs.getString("name"));
            transaction.setAmount(rs.getDouble("amount"));
            transaction.setDate(rs.getDate("date").toLocalDate());
            transaction.setAccountOwner(rs.getString("account_owner"));
            return transaction;
        }
    }
}