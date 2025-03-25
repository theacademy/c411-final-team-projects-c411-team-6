package org.mthree.controllers;

import org.mthree.service.PlaidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/plaid")
public class PlaidController {

    @Autowired
    private PlaidService plaidService;

    @PostMapping("/create-public-token")
    public ResponseEntity<String> createPublicToken() {
        try {
            String publicToken = plaidService.createSandboxPublicToken();
            return ResponseEntity.ok(publicToken);
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()).toString());
        }
    }

    @PostMapping("/exchange-public-token")
    public ResponseEntity<Map<String, String>> exchangePublicToken(@RequestBody Map<String, String> requestBody) {
        String publicToken = requestBody.get("public_token");
        String accessToken = null;
        try {
            accessToken = plaidService.exchangePublicToken(publicToken);
        } catch (IOException e) {
            System.out.println("Error exchanging public token: " + e.getMessage());
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Error exchanging public token"));
        }

        Map<String, String> response = new HashMap<>();
        response.put("access_token", accessToken);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/create-link-token")
    public ResponseEntity<String> createLinkToken(@RequestParam String userId) {
        String linkToken;
        try{
            linkToken = plaidService.createLinkToken(userId);
            return ResponseEntity.ok(linkToken);
        } catch(IOException e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()).toString());
        }
    }
}
