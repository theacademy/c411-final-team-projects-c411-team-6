package org.mthree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppTest {
    @Value("${plaid.client.id}")
    String clientId;

    @Test
    void checkClientId() {
        System.out.println("Client ID: " + clientId);
        Assertions.assertNotNull(clientId);
    }
}
