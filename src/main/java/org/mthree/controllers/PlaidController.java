package org.mthree.controllers;

import org.mthree.service.PlaidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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

    @PostMapping("/exchange-token")
    public ResponseEntity<String> exchangeToken(@RequestBody Map<String, String> body) {
        try {
            String publicToken = body.get("public_token");
            String accessToken = plaidService.exchangePublicToken(publicToken);
            return ResponseEntity.ok(accessToken);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()).toString());
        }
    }

    @PostMapping("/create-link-token")
    public ResponseEntity<String> createLinkToken(){
        String linkToken;
        try{
            linkToken = plaidService.createLinkToken();
            return ResponseEntity.ok(linkToken);
        } catch(IOException e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()).toString());
        }
    }
}
