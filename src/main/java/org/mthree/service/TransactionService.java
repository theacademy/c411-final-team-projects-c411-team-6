package org.mthree.service;

import com.plaid.client.model.TransactionsGetRequest;
import com.plaid.client.model.TransactionsGetResponse;
import com.plaid.client.request.PlaidApi;
import org.mthree.dto.Item;
import org.mthree.dto.Transaction;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {
    private final PlaidApi plaidApi;
    private final ItemService itemService;

    public TransactionService(PlaidApi plaidApi, ItemService itemService) {
        this.plaidApi = plaidApi;
        this.itemService = itemService;
    }

    public List<Transaction> getTransactions(Long userId) {
        List<Item> userItems = itemService.getItemsById(userId.toString());
        List<Transaction> allTxns = new ArrayList<>();

        for (Item item : userItems) {
            try {
                TransactionsGetRequest request = new TransactionsGetRequest()
                        .accessToken(item.getPlaidAccessToken())
                        .startDate(LocalDate.now().minusMonths(1))
                        .endDate(LocalDate.now());

                TransactionsGetResponse response = plaidApi.transactionsGet(request).execute().body();

                response.getTransactions().forEach(plaidTxn -> {
                    Transaction txn = new Transaction();
                    txn.setName(plaidTxn.getName());
                    txn.setAmount(plaidTxn.getAmount());
                    txn.setDate(plaidTxn.getDate());
                    txn.setUserId(userId);
                    txn.setType(plaidTxn.getTransactionType().toString());
                    txn.setCategory(plaidTxn.getCategory() != null && !plaidTxn.getCategory().isEmpty()
                            ? plaidTxn.getCategory().get(0)
                            : "Unknown");
                    txn.setPending(plaidTxn.getPending());

                    allTxns.add(txn);
                });
            } catch (IOException e) {
                throw new RuntimeException("Error fetching transactions for userId " + userId, e);
            }

        }
        return allTxns;
    }

    public List<Transaction> getTransactionsByDate(Long userId, LocalDate startDate, LocalDate endDate) {


        List<Item> userItems = itemService.getItemsById(userId.toString());
        List<Transaction> allTxns = new ArrayList<>();

        for (Item item : userItems) {
            try {
                TransactionsGetRequest request = new TransactionsGetRequest()
                        .accessToken(item.getPlaidAccessToken())
                        .startDate(startDate)
                        .endDate(endDate);

                TransactionsGetResponse response = plaidApi.transactionsGet(request).execute().body();

                response.getTransactions().forEach(plaidTxn -> {
                    Transaction txn = new Transaction();
                    txn.setName(plaidTxn.getName());
                    txn.setAmount(plaidTxn.getAmount());
                    txn.setDate(plaidTxn.getDate());
                    txn.setUserId(userId);
                    txn.setType(plaidTxn.getTransactionType().toString());
                    txn.setCategory(plaidTxn.getCategory() != null && !plaidTxn.getCategory().isEmpty()
                            ? plaidTxn.getCategory().get(0)
                            : "Unknown");
                    txn.setPending(plaidTxn.getPending());

                    allTxns.add(txn);
                });
            } catch (IOException e) {
                throw new RuntimeException("Error fetching transactions for userId " + userId, e);
            }

        }
        return allTxns;

    }

    public List<Transaction> getRevenueTransactions(List<Transaction> allTransactions) {

        return allTransactions.stream()
                .filter(txn -> !txn.getCategory().equalsIgnoreCase("Payment") || !txn.getCategory().equalsIgnoreCase("Travel") || !txn.getCategory().equalsIgnoreCase("Shops"))
                .toList();

    }

    public List<Transaction> getExpenseTransactions(List<Transaction> allTransactions) {

        return allTransactions.stream()
                .filter(txn -> txn.getCategory().equalsIgnoreCase("Payment") || txn.getCategory().equalsIgnoreCase("Travel") || txn.getCategory().equalsIgnoreCase("Shops"))
                .toList();

    }


}
