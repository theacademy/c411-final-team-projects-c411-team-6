package org.mthree.controllers;

import com.plaid.client.model.TransactionsGetRequest;
import com.plaid.client.request.PlaidApi;
import org.mthree.dto.Transaction;
import org.mthree.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RequestMapping("/transactions")
@RestController
public class TransactionController {
    private final TransactionService transactionService;
    private final PlaidApi plaidApi;

    public TransactionController(TransactionService transactionService, PlaidApi plaidApi) {
        this.transactionService = transactionService;
        this.plaidApi = plaidApi;

    }

    // Get all transactions by userId
    @GetMapping("")
    public ResponseEntity<List<Transaction>> getAllTransactions(@RequestParam String userId) {
        try {
            System.out.println("IN transactions");
            List<Transaction> items = transactionService.getTransactions(userId);
            System.out.println("Returning transactions");
            return ResponseEntity.ok(items);
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

}
