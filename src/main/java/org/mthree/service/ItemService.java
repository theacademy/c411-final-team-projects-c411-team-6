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
public class ItemService {

    private final ItemDao itemDao;
    private final PlaidApi plaidApi;

    public ItemService(ItemDao itemDao, PlaidApi plaidApi) {
        this.itemDao = itemDao;
        this.plaidApi = plaidApi;
    }

    public String createSandboxPublicToken() throws IOException {

        SandboxPublicTokenCreateRequestOptions options = new SandboxPublicTokenCreateRequestOptions()
                .overrideUsername("custom_1")
                .overridePassword("custom_1");

        SandboxPublicTokenCreateRequest request = new SandboxPublicTokenCreateRequest()
                .institutionId("ins_109508")
                .initialProducts(List.of(Products.AUTH, Products.TRANSACTIONS));

        request.setOptions(options);


        Response<SandboxPublicTokenCreateResponse> response =
                plaidApi.sandboxPublicTokenCreate(request).execute();

        if (response.isSuccessful()) {
            return response.body().getPublicToken();
        } else {
            throw new RuntimeException("Failed to create sandbox public token: " + response.errorBody().string());
        }
    }


    public String exchangePublicToken(String publicToken) throws IOException {
        ItemPublicTokenExchangeRequest request = new ItemPublicTokenExchangeRequest()
                .publicToken(publicToken);

        Response<ItemPublicTokenExchangeResponse> response =
                plaidApi.itemPublicTokenExchange(request).execute();

        if (response.isSuccessful()) {
            return response.body().getAccessToken();
        } else {
            throw new RuntimeException("Failed to exchange public token: " + response.errorBody().string());
        }
    }


    public String createLinkToken(String userId) throws IOException {
        LinkTokenCreateRequestUser user = new LinkTokenCreateRequestUser()
                .clientUserId(userId);

        LinkTokenCreateRequest request = new LinkTokenCreateRequest()
                .user(user)
                .clientName("Your App")
                .products(Arrays.asList(Products.TRANSACTIONS))
                .countryCodes(Arrays.asList(CountryCode.US))
                .language("en");

        Response<LinkTokenCreateResponse> response = plaidApi.linkTokenCreate(request).execute();

        if (response.isSuccessful()) {
            return response.body().getLinkToken();
        } else {
            throw new RuntimeException("Error creating link token: " + response.errorBody().string());
        }
    }

    public void addPlaidItem(Item item){
        System.out.println("In SERVICE ADD PLAID ITEM Adding plaid item: " + item);
        item.setCreatedAt(LocalDateTime.now());
        itemDao.addItem(item);
    }

    public List<Item> getItemsById(String id){
        System.out.println("SERVICE Getting items");
        return itemDao.findItemsByUserId(id);
    }
}
