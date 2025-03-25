package org.mthree.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlaidService {


    @Value("${plaid.client.id}")
    private String clientId;

    @Value("${plaid.secret}")
    private String secret;

    private final String baseUrl = "https://sandbox.plaid.com";

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
}
