package org.mthree.service;

import com.plaid.client.model.*;
import com.plaid.client.request.PlaidApi;
import org.mthree.dao.ItemDao;
import org.mthree.dto.Item;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public interface ItemService {
     String createSandboxPublicToken() throws IOException;
     String exchangePublicToken(String publicToken) throws IOException;
     String createLinkToken(String userId) throws IOException;
     void addPlaidItem(Item item);
     List<Item> getItemsById(String id);
}
