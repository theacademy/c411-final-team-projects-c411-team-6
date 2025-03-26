package org.mthree.controllers;

import com.plaid.client.model.ItemGetRequest;
import com.plaid.client.model.ItemGetResponse;
import com.plaid.client.request.PlaidApi;
import org.mthree.dto.Item;
import org.mthree.service.ItemService;
import org.mthree.service.PlaidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/plaid")
public class PlaidController {

    @Autowired
    private PlaidService plaidService;
    @Autowired
    private PlaidApi plaidApi;
    @Autowired
    private ItemService itemService;

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
        String accessToken;

        try {
            accessToken = plaidService.exchangePublicToken(publicToken);

            ItemGetRequest itemRequest = new ItemGetRequest().accessToken(accessToken);
            ItemGetResponse itemResponse = plaidApi.itemGet(itemRequest).execute().body();

            String itemId = itemResponse.getItem().getItemId();
            String institutionId = itemResponse.getItem().getInstitutionId();

            System.out.println("accessToken = " + accessToken);
            System.out.println("itemId = " + itemId);
            System.out.println("institutionId = " + institutionId);

            Map<String, String> response = new HashMap<>();
            response.put("access_token", accessToken);
            response.put("item_id", itemId);
            response.put("institution_id", institutionId);


            Item item = new Item();
            item.setUserId(1L);
            item.setPlaidAccessToken(accessToken);
            item.setPlaidItemId(itemId);

            System.out.println("Adding to item");
            itemService.addPlaidItem(item);
            System.out.println("Added plaid item");
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Error exchanging public token"));
        }
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

    @GetMapping("/get-items")
    public ResponseEntity<List<Item>> getItems(@RequestParam String userId) {
        try {
            List<Item> items = itemService.getItemsById(userId);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            e.printStackTrace(); // 👈 this will print the real cause
            return ResponseEntity.status(500).build();
        }
    }
}