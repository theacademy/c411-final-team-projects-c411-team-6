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


public interface TransactionService {
    public List<Transaction> getTransactions(Long userId);

}
