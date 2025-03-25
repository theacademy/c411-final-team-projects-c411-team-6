package org.mthree.service;


import com.plaid.client.model.*;
import com.plaid.client.request.PlaidApi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import retrofit2.Response;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlaidService {

    private final PlaidApi plaidApi;

    @Value("${plaid.client.id}")
    private String clientId;

    @Value("${plaid.secret}")
    private String secret;

    private final String baseUrl = "https://sandbox.plaid.com";

    public PlaidService(PlaidApi plaidApi){
        this.plaidApi = plaidApi;
    }
    public String createSandboxPublicToken() throws IOException {
        SandboxPublicTokenCreateRequest request = new SandboxPublicTokenCreateRequest()
                .institutionId("ins_109508")
                .initialProducts(List.of(Products.AUTH, Products.TRANSACTIONS));

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


    public String createLinkToken() throws IOException {
        LinkTokenCreateRequestUser user = new LinkTokenCreateRequestUser()
                .clientUserId("user-id-123"); // Use a real unique user ID in prod

        LinkTokenCreateRequest request = new LinkTokenCreateRequest()
                .user(user)
                .clientName("FlowTrack")
                .products(List.of(com.plaid.client.model.Products.AUTH)) // add other products as needed
                .countryCodes(List.of(CountryCode.US))
                .language("en");

        Response<LinkTokenCreateResponse> response = plaidApi.linkTokenCreate(request).execute();

        if (response.isSuccessful()) {
            return response.body().getLinkToken();
        } else {
            throw new RuntimeException("Error creating link token: " + response.errorBody().string());
        }
    }

}
