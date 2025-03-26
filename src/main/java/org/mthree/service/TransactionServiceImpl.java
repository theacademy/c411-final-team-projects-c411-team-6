package org.mthree.service;

import com.plaid.client.model.TransactionsGetRequest;
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
public class TransactionServiceImpl implements TransactionService {
    private final PlaidApi plaidApi;
    private final ItemService itemService;
    private final TransactionDao transactionDao;

    public TransactionServiceImpl(PlaidApi plaidApi, ItemService itemService,  TransactionDao transactionDao) {
        this.plaidApi = plaidApi;
        this.itemService = itemService;
        this.transactionDao = transactionDao;
    }

    @Override
    public void addTransaction(Transaction txn){
        transactionDao.createTransaction(txn);
    }

    @Override
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
                if (!response.getTransactions().isEmpty()) {
                    response.getTransactions().forEach(plaidTxn -> {
                        Transaction txn = new Transaction();
                        txn.setId(plaidTxn.getTransactionId().toString());
                        txn.setName(plaidTxn.getName());
                        txn.setAmount(plaidTxn.getAmount());
                        txn.setDate(plaidTxn.getDate());
                        txn.setUserId(userId);
                        txn.setType(plaidTxn.getTransactionType().toString());
                        txn.setPending(plaidTxn.getPending());
                        txn.setCategory(plaidTxn.getCategory() != null && !plaidTxn.getCategory().isEmpty()
                                ? plaidTxn.getCategory().get(0)
                                : "Unknown");
                        txn.setPending(plaidTxn.getPending());
                        //addTransaction(txn);
                        allTxns.add(txn);
                    });
                }
            } catch (IOException e) {
                throw new RuntimeException("Error fetching transactions for userId " + userId, e);
            }

        }
        return allTxns;
    }

}
