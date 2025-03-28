package org.mthree.service;

import com.plaid.client.model.TransactionsGetRequest;
import com.plaid.client.model.TransactionsGetRequestOptions;
import com.plaid.client.model.TransactionsGetResponse;
import com.plaid.client.request.PlaidApi;
import org.mthree.dao.TransactionDao;
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
    private final TransactionDao transactionDao;

    public TransactionService(PlaidApi plaidApi, ItemService itemService, TransactionDao transactionDao) {
        this.plaidApi = plaidApi;
        this.itemService = itemService;
        this.transactionDao = transactionDao;
    }

    public List<Transaction> getTransactions(String userId, int months) {
        List<Transaction> cachedTransactions = transactionDao.getTransactionsByUserId(userId, months);
        if (!cachedTransactions.isEmpty()) {
            return cachedTransactions;
        }

        List<Item> userItems = itemService.getItemsById(userId);
        List<Transaction> allTransactions = new ArrayList<>();

        for (Item item : userItems) {
            try {
                LocalDate startDate = LocalDate.now().minusMonths(months);
                LocalDate endDate = LocalDate.now();

                TransactionsGetRequest request = new TransactionsGetRequest()
                        .accessToken(item.getPlaidAccessToken())
                        .startDate(startDate)
                        .endDate(endDate)
                        .options(new TransactionsGetRequestOptions().count(500));

                TransactionsGetResponse response = plaidApi.transactionsGet(request).execute().body();
                if (response == null) {
                    throw new IOException("Null response from Plaid API");
                }

                response.getTransactions().forEach(plaidTransaction -> {
                    Transaction transaction = new Transaction();
                    transaction.setPlaidTransactionId(plaidTransaction.getTransactionId());
                    transaction.setName(plaidTransaction.getName());
                    transaction.setAmount(plaidTransaction.getAmount());
                    transaction.setDate(plaidTransaction.getDate());
                    transaction.setAccountOwner(userId);
                    transaction.setType(plaidTransaction.getTransactionType().toString());
                    transaction.setCategory(plaidTransaction.getCategory() != null && !plaidTransaction.getCategory().isEmpty()
                            ? plaidTransaction.getCategory().get(0)
                            : "Unknown");
                    transaction.setPending(plaidTransaction.getPending());

                    allTransactions.add(transaction);
                    transactionDao.saveTransaction(transaction);
                });

                System.out.println("Fetched and saved " + response.getTransactions().size() + " transactions for user " + userId);
            } catch (IOException e) {
                throw new RuntimeException("Error fetching transactions for userId " + userId, e);
            }
        }
        return allTransactions;
    }

    public List<Transaction> getTransactions(String userId) {
        List<Item> userItems = itemService.getItemsById(userId);
        List<Transaction> allTransactions = new ArrayList<>();

        for (Item item : userItems) {
            try {
                TransactionsGetRequest request = new TransactionsGetRequest()
                        .accessToken(item.getPlaidAccessToken())
                        .startDate(LocalDate.now().minusMonths(1))
                        .endDate(LocalDate.now());

                TransactionsGetResponse response = plaidApi.transactionsGet(request).execute().body();

                response.getTransactions().forEach(plaidtransaction -> {
                    Transaction transaction = new Transaction();
                    transaction.setName(plaidtransaction.getName());
                    transaction.setAmount(plaidtransaction.getAmount());
                    transaction.setDate(plaidtransaction.getDate());
                    transaction.setAccountOwner(userId);
                    transaction.setType(plaidtransaction.getTransactionType().toString());
                    transaction.setCategory(plaidtransaction.getCategory() != null && !plaidtransaction.getCategory().isEmpty()
                            ? plaidtransaction.getCategory().get(0)
                            : "Unknown");
                    transaction.setPending(plaidtransaction.getPending());

                    allTransactions.add(transaction);
                });
            } catch (IOException e) {
                throw new RuntimeException("Error fetching transactions for userId " + userId, e);
            }

        }
        return allTransactions;
    }


}
