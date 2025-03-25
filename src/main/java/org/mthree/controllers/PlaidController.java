package org.mthree.controllers;

import org.mthree.service.PlaidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/plaid")
public class PlaidController {

    @Autowired
    private PlaidService plaidService;

    @PostMapping("/create-public-token")
    public ResponseEntity<String> createPublicToken() {
        String publicToken = plaidService.createSandboxPublicToken();
        return ResponseEntity.ok(publicToken);
    }

    @PostMapping("/exchange-token")
    public ResponseEntity<String> exchangeToken(@RequestBody String publicToken) {
        String accessToken = plaidService.exchangePublicToken(publicToken);
        return ResponseEntity.ok(accessToken);
    }
}
