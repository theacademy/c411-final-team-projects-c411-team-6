package org.mthree.service;

import com.plaid.client.model.SandboxPublicTokenCreateRequest;
import com.plaid.client.request.PlaidApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mthree.dao.ItemDao;
import org.mthree.dto.Item;
import org.mthree.dto.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ForecastServiceImplTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private ForecastServiceImpl forecastService;

    private Transaction sampleTransaction;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleTransaction = new Transaction();
        sampleTransaction.setPlaidTransactionId("plaidTransactionId");
        sampleTransaction.setId((long) 1);
        sampleTransaction.setCategory("Food");
        sampleTransaction.setType("Place");
        sampleTransaction.setName("KFC");
        sampleTransaction.setAmount(100.00);
        sampleTransaction.setDate(LocalDate.now());
        sampleTransaction.setPending(false);
        sampleTransaction.setAccountOwner("John");

    }

    @Test
    void testForecastSpending() throws IOException {
        // Arrange
        when(transactionService.getTransactions("1", 1)).thenReturn(List.of(sampleTransaction));

        // Act
        Map<String, Double> result = forecastService.forecastSpending("1", 1, 1);

        // Assert
        assertNotNull(result);

    }


}