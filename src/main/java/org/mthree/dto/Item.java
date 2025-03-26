package org.mthree.dto;


import java.time.LocalDateTime;

public class Item {
    private Long id;
    private Long userId;
    private String plaidAccessToken;
    private String plaidItemId;
    private LocalDateTime createdAt;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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

