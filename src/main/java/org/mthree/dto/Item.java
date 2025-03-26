package org.mthree.dto;


import java.time.LocalDateTime;

public class Item {
    private int id;
    private int userId;
    private String plaidAccessToken;
    private String plaidItemId;
    private LocalDateTime createdAt;

    public Item() {}

    public Item(String plaidAccessToken, String plaidItemId, LocalDateTime now) {
        this.plaidAccessToken = plaidAccessToken;
        this.plaidItemId = plaidItemId;
        this.createdAt = now;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPlaidAccessToken() {
        return plaidAccessToken;
    }

    public void setPlaidAccessToken(String plaidAccessToken) {
        this.plaidAccessToken = plaidAccessToken;
    }

    public String getPlaidItemId() {
        return plaidItemId;
    }

    public void setPlaidItemId(String plaidItemId) {
        this.plaidItemId = plaidItemId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}

