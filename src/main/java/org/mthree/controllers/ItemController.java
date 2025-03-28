package org.mthree.controllers;

import com.plaid.client.model.AccountsGetRequest;
import com.plaid.client.model.AccountsGetResponse;
import com.plaid.client.model.ItemGetRequest;
import com.plaid.client.model.ItemGetResponse;
import com.plaid.client.request.PlaidApi;
import org.mthree.dto.Item;
import org.mthree.service.ItemService;
import org.mthree.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/plaid")
public class ItemController {

    @Autowired
    private PlaidApi plaidApi;
    @Autowired
    private ItemService itemService;
    @Autowired
    private TransactionService transactionService;

    //Creates public token
    @PostMapping("/create-public-token")
    public ResponseEntity<String> createPublicToken() {
        try {
            String publicToken = itemService.createSandboxPublicToken();
            return ResponseEntity.ok(publicToken);
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()).toString());
        }
    }

    @PostMapping("/exchange-public-token")
    public ResponseEntity<Map<String, String>> exchangePublicToken(@RequestBody Map<String, String> requestBody) {
        System.out.println("Received request: " + requestBody);

        if (!requestBody.containsKey("public_token") || !requestBody.containsKey("user_id")) {
            System.out.println("Missing public_token or user_id");
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Missing public_token or user_id"));
        }

        String publicToken = requestBody.get("public_token");
        String userId = requestBody.get("user_id");

        System.out.println("Extracted Public Token: " + publicToken);
        System.out.println("Extracted User ID: " + userId);

        try {
            String accessToken = itemService.exchangePublicToken(publicToken);

            ItemGetRequest itemRequest = new ItemGetRequest().accessToken(accessToken);
            ItemGetResponse itemResponse = plaidApi.itemGet(itemRequest).execute().body();

            if (itemResponse == null || itemResponse.getItem() == null) {
                System.out.println("Failed to retrieve item details from Plaid");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonMap("error", "Failed to retrieve item details"));
            }

            String itemId = itemResponse.getItem().getItemId();
            String institutionId = itemResponse.getItem().getInstitutionId();

            Item item = new Item();
            item.setUserId(Integer.parseInt(userId));
            item.setPlaidAccessToken(accessToken);
            item.setPlaidItemId(itemId);

            itemService.addPlaidItem(item);
            transactionService.getTransactions(userId, 6);

            Map<String, String> response = new HashMap<>();
            response.put("access_token", accessToken);
            response.put("item_id", itemId);
            response.put("institution_id", institutionId);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            System.out.println("Error exchanging public token: " + e.getMessage());
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Error exchanging public token"));
        }
    }

    @PostMapping("/create-link-token")
    public ResponseEntity<String> createLinkToken(@RequestBody Map<String, String> body) {
        String userId = body.get("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().body("Missing userId in request body");
        }
        String linkToken;
        try {
            linkToken = itemService.createLinkToken(userId);
            return ResponseEntity.ok(linkToken);
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()).toString());
        }
    }

    //Gets all items under a userId
    @GetMapping("/get-items")
    public ResponseEntity<List<Item>> getItems(@RequestParam String userId) {
        try {
            List<Item> items = itemService.getItemsById(userId);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // Get all bank accounts under user
    @GetMapping("/accounts/{userId}")
    public ResponseEntity<?> getAccounts(@PathVariable String userId) {
        List<Item> userItems = itemService.getItemsById(userId);

        if (userItems == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No items found.");
        }

        List<Object> allAccounts = new ArrayList<>();
        for (Item item : userItems) {
                try {
                    AccountsGetRequest request = new AccountsGetRequest().accessToken(item.getPlaidAccessToken());
                    AccountsGetResponse response = plaidApi.accountsGet(request).execute().body();

                    allAccounts.addAll(response.getAccounts());
                } catch (IOException e) {
                    System.out.println("Failed to fetch accounts for access token: " + item.getPlaidAccessToken());
                    e.printStackTrace();
                }
            }

        return ResponseEntity.ok(allAccounts);
    }
}