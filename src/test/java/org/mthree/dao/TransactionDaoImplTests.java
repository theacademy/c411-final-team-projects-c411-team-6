package org.mthree.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mthree.dto.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TransactionDaoImplTests {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private TransactionDaoImpl transactionDao;

    @BeforeEach
    public void setUp() {
    }

    @Test
    @DisplayName("Save Transaction Test")
    public void saveTransactionTest() {
        Transaction transaction = new Transaction();
        transaction.setPlaidTransactionId("txn-123");
        transaction.setCategory("Food");
        transaction.setType("debit");
        transaction.setName("Test Purchase");
        transaction.setAmount(50.00);
        transaction.setDate(LocalDate.now());
        transaction.setAccountOwner("1");

        when(jdbcTemplate.update(
                eq("INSERT INTO transactions (plaid_transaction_id, account_id, category, type, name, amount, date, account_owner) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE amount = VALUES(amount)"),
                eq("txn-123"),
                isNull(),
                eq("Food"),
                eq("debit"),
                eq("Test Purchase"),
                eq(50.00),
                any(LocalDate.class),
                eq("1")
        )).thenReturn(1);

        transactionDao.saveTransaction(transaction);

        verify(jdbcTemplate, times(1)).update(
                eq("INSERT INTO transactions (plaid_transaction_id, account_id, category, type, name, amount, date, account_owner) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE amount = VALUES(amount)"),
                eq("txn-123"),
                isNull(),
                eq("Food"),
                eq("debit"),
                eq("Test Purchase"),
                eq(50.00),
                any(LocalDate.class),
                eq("1")
        );
    }

    @Test
    @DisplayName("Get Transactions By User ID Test")
    public void getTransactionsByUserIdTest() {
        Transaction transaction = new Transaction();
        transaction.setPlaidTransactionId("txn-456");
        transaction.setCategory("Travel");
        transaction.setType("debit");
        transaction.setName("Trip");
        transaction.setAmount(200.00);
        transaction.setDate(LocalDate.now().minusDays(5));
        transaction.setAccountOwner("2");
        List<Transaction> transactions = Arrays.asList(transaction);

        when(jdbcTemplate.query(
                eq("SELECT * FROM transactions WHERE account_owner = ? AND date >= ?"),
                any(RowMapper.class),
                eq("2"),
                any(LocalDate.class)
        )).thenReturn(transactions);

        List<Transaction> result = transactionDao.getTransactionsByUserId("2", 1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Travel", result.get(0).getCategory());
        verify(jdbcTemplate, times(1)).query(
                eq("SELECT * FROM transactions WHERE account_owner = ? AND date >= ?"),
                any(RowMapper.class),
                eq("2"),
                any(LocalDate.class)
        );
    }
}