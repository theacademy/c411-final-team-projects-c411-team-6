package org.mthree.controllers;

import com.plaid.client.model.AccountsGetRequest;
import com.plaid.client.model.AccountsGetResponse;
import com.plaid.client.model.ItemGetRequest;
import com.plaid.client.model.ItemGetResponse;
import com.plaid.client.request.PlaidApi;
import org.mthree.dto.Item;
import org.mthree.service.ItemService;
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

    // Exchanges public token to get access token
    @PostMapping("/exchange-public-token")
    public ResponseEntity<Map<String, String>> exchangePublicToken(@RequestBody Map<String, String> requestBody) {
        String publicToken = requestBody.get("public_token");
        String accessToken;

        try {
            accessToken = itemService.exchangePublicToken(publicToken);

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

    //Creates our link token
    @PostMapping("/create-link-token")
    public ResponseEntity<String> createLinkToken(@RequestParam String userId) {
        String linkToken;
        try{
            linkToken = itemService.createLinkToken(userId);
            return ResponseEntity.ok(linkToken);
        } catch(IOException e){
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
            e.printStackTrace(); // 👈 this will print the real cause
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