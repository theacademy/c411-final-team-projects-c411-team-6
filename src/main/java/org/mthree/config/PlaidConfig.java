package org.mthree.config;

import com.plaid.client.ApiClient;
import com.plaid.client.request.PlaidApi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class PlaidConfig {
    @Value("${plaid.client.id}")
    private String clientId;

    @Value("${plaid.secret}")
    private String secret;

    @Value("${plaid.env:sandbox}")
    private String plaidEnv;

    @Bean
    public PlaidApi plaidApi() {
        switch (plaidEnv) {
            case "sandbox":
                plaidEnv = ApiClient.Sandbox;
                break;
            case "production":
                plaidEnv = ApiClient.Production;
                break;
            default:
                plaidEnv = ApiClient.Sandbox;
        }

        HashMap<String, String> apiKeys = new HashMap<String, String>();
        apiKeys.put("clientId", clientId);
        apiKeys.put("secret", secret);
        apiKeys.put("plaidVersion", "2020-09-14");
        ApiClient apiClient = new ApiClient(apiKeys);
        apiClient.setPlaidAdapter(plaidEnv);

        return apiClient.createService(PlaidApi.class);
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getEnv() {
        return plaidEnv;
    }

    public void setEnv(String env) {
        this.plaidEnv = env;
    }
}
