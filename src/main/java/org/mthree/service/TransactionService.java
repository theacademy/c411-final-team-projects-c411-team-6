package org.mthree.service;

import com.plaid.client.model.TransactionsGetRequest;
import com.plaid.client.model.TransactionsGetResponse;
import com.plaid.client.request.PlaidApi;
import org.mthree.dto.Item;
import org.mthree.dto.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Response;

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

    public List<Transaction> getTransactions(String userId) {
        List<Item> userItems = itemService.getItemsById(userId);
        List<Transaction> allTxns = new ArrayList<>();

        for (Item item : userItems) {
            try {
                TransactionsGetRequest request = new TransactionsGetRequest()
                        .accessToken(item.getPlaidAccessToken())
                        .startDate(LocalDate.now().minusMonths(1))
                        .endDate(LocalDate.now());
                System.out.println("Getting transactions SERVICE");
                TransactionsGetResponse response = plaidApi.transactionsGet(request).execute().body();

                Response<TransactionsGetResponse> plaidResponse = plaidApi.transactionsGet(request).execute();

                if (!plaidResponse.isSuccessful()) {
                    System.out.println("Plaid response error: " +
                            (plaidResponse.errorBody() != null ? plaidResponse.errorBody().string() : "No error body"));
                } else {
                    System.out.println("Plaid call successful");
                }

                System.out.println("Plaid response code: " + plaidResponse.code());
                System.out.println("Plaid response error: " + plaidResponse.errorBody() != null ? plaidResponse.errorBody().string() : "No error body");

                if (response == null) {
                    System.out.println("Warning: plaidResponse.body() is null for item: " + item.getPlaidItemId());
                } else if (response.getTransactions() == null) {
                    System.out.println("Warning: getTransactions() is null for item: " + item.getPlaidItemId());
                }

                System.out.println("Item: " + item.getPlaidAccessToken());
                response.getTransactions().forEach(plaidTxn -> {
                    Transaction txn = new Transaction();
                    txn.setName(plaidTxn.getName());
                    txn.setAmount(plaidTxn.getAmount());
                    txn.setDate(plaidTxn.getDate());
                    txn.setCategory(plaidTxn.getCategory() != null && !plaidTxn.getCategory().isEmpty()
                            ? plaidTxn.getCategory().get(0)
                            : "Unknown");
                    txn.setPending(plaidTxn.getPending());


                    allTxns.add(txn);
                });
                System.out.println("Got our items");
            } catch (IOException e) {
                System.out.println("Error fetching transactions for userId " + userId + ": " + e.getMessage());
            }
        }

        return allTxns;
    }


}
