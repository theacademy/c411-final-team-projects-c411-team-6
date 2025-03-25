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
    public String createSandboxPublicToken() {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrl + "/sandbox/public_token/create";

        Map<String, Object> body = new HashMap<>();
        body.put("client_id", clientId);
        body.put("secret", secret);
        body.put("institution_id", "ins_109508"); // Sandbox test bank
        body.put("initial_products", List.of("auth", "transactions"));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        return (String) response.getBody().get("public_token");
    }

    public String exchangePublicToken(String publicToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrl + "/item/public_token/exchange";

        Map<String, Object> body = new HashMap<>();
        body.put("client_id", clientId);
        body.put("secret", secret);
        body.put("public_token", publicToken);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        return (String) response.getBody().get("access_token");
    }


    public String createLinkToken() throws IOException {
        LinkTokenCreateRequestUser user = new LinkTokenCreateRequestUser()
                .clientUserId("user-id-123"); // Use a real unique user ID in prod

        LinkTokenCreateRequest request = new LinkTokenCreateRequest()
                .user(user)
                .clientName("FlowChart")
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
