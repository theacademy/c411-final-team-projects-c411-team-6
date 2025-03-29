package org.mthree.controllers;

import com.plaid.client.model.TransactionsGetRequest;
import com.plaid.client.request.PlaidApi;
import org.mthree.dto.Transaction;
import org.mthree.service.ForecastService;
import org.mthree.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@RequestMapping("/transactions")
@RestController
public class TransactionController {
    private final TransactionService transactionService;
    private final ForecastService forecastService;
    private final PlaidApi plaidApi;

    public TransactionController(TransactionService transactionService, ForecastService forecastService, PlaidApi plaidApi) {
        this.transactionService = transactionService;
        this.forecastService = forecastService;
        this.plaidApi = plaidApi;
    }

    // Get all transactions by userId
    @GetMapping("")
    public ResponseEntity<List<Transaction>> getAllTransactions(@RequestParam String userId) {
        try {
            List<Transaction> items = transactionService.getTransactions(userId);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // Get all transactions by date
    @GetMapping("/by-date")
    public ResponseEntity<List<Transaction>> getTransactionsByDateRange(
            @RequestParam String userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            List<Transaction> all = transactionService.getTransactions(userId);
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            List<Transaction> filtered = all.stream()
                    .filter(transaction -> !transaction.getDate().isBefore(start) && !transaction.getDate().isAfter(end))
                    .toList();

            return ResponseEntity.ok(filtered);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // Get all transactions by category
    @GetMapping("/by-category")
    public ResponseEntity<List<Transaction>> getTransactionsByCategory(
            @RequestParam String userId,
            @RequestParam String category) {
        try {
            List<Transaction> all = transactionService.getTransactions(userId);
            List<Transaction> filtered = all.stream()
                    .filter(transaction -> transaction.getCategory().equalsIgnoreCase(category))
                    .toList();
            return ResponseEntity.ok(filtered);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/debug")
    public ResponseEntity<?> debugTransactions(@RequestParam String accessToken) {
        try {
            TransactionsGetRequest request = new TransactionsGetRequest()
                    .accessToken(accessToken)
                    .startDate(LocalDate.now().minusMonths(1))
                    .endDate(LocalDate.now());

            var response = plaidApi.transactionsGet(request).execute();

            if (response.isSuccessful() && response.body() != null) {
                return ResponseEntity.ok(response.body().getTransactions());
            } else {
                String errorMessage = response.errorBody() != null
                        ? response.errorBody().string()
                        : "Unknown error (no error body)";
                return ResponseEntity.status(response.code()).body("Plaid API error: " + errorMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal error: " + e.getMessage());
        }
    }

    @GetMapping("/forecast")
    public ResponseEntity<Map<String, Object>> getSpendingForecast(
            @RequestParam String userId,
            @RequestParam(defaultValue = "6") int historicalMonths,
            @RequestParam(defaultValue = "3") int forecastMonths) {
        try {
            Map<String, Object> forecast = forecastService.forecastSpending(userId, historicalMonths, forecastMonths);
            return ResponseEntity.ok(forecast);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

}
