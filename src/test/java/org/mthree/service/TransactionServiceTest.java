package org.mthree.service;

import com.plaid.client.model.TransactionsGetRequest;
import com.plaid.client.model.TransactionsGetResponse;
import com.plaid.client.request.PlaidApi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mthree.dao.ItemDaoImpl;
import org.mthree.dao.TransactionDao;
import org.mthree.dao.TransactionDaoImpl;
import org.mthree.dao.UserDao;
import org.mthree.dto.Item;
import org.mthree.dto.Transaction;
import org.mthree.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import retrofit2.Call;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {


    @Mock
    private ItemService itemService;

    @Mock
    private TransactionDaoImpl transactionDao;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction sampleTransaction;
    private Item sampleItem;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleItem = new Item("plaidAccessToken", "plaidItemId", LocalDateTime.now());
        sampleItem.setUserId(1);
        sampleItem.setId(1);

        sampleTransaction = new Transaction();
        sampleTransaction.setPlaidTransactionId("plaidTransactionId");
        sampleTransaction.setId(1);
        sampleTransaction.setCategory("Food");
        sampleTransaction.setType("Place");
        sampleTransaction.setName("KFC");
        sampleTransaction.setAmount(100.00);
        sampleTransaction.setDate(LocalDate.now());
        sampleTransaction.setPending(false);
        sampleTransaction.setAccountOwner("John");


    }

    @Test
    void testGetTransactions_Existing() throws IOException {
        // Arrange
        when(itemService.getItemsById("1")).thenReturn(List.of(sampleItem));
        when(transactionDao.getTransactionsByUserId("1", 1)).thenReturn(List.of(sampleTransaction));


        // Act
        List<Transaction> result = transactionService.getTransactions("1", 1);

        // Assert
        assertNotNull(result);
        assertEquals("KFC", result.get(0).getName());
        verify(transactionDao, times(1)).getTransactionsByUserId("1", 1);

    }

    @Test
    void testGetTransactions_NotExisting() throws IOException {
        // Arrange
        when(itemService.getItemsById("1")).thenReturn(List.of(sampleItem));
        when(transactionDao.getTransactionsByUserId("1", 1)).thenReturn(List.of(sampleTransaction));

        // Act
        List<Transaction> result = transactionService.getTransactions("1", 1);

        // Assert
        assertNotNull(result);

        verify(transactionDao, times(1)).getTransactionsByUserId("1", 1);

    }




}