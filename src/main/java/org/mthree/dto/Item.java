package org.mthree.dto;



public class Item {


    private Long id;
    private Long userId;
    private String plaidAccessToken;
    private String plaidItemId;
    private String plaidInstitutionId;
    private String status;


    public Item(Long userId, String plaidAccessToken, String plaidItemId, String plaidInstitutionId, String status) {
        this.userId = userId;
        this.plaidAccessToken = plaidAccessToken;
        this.plaidItemId = plaidItemId;
        this.plaidInstitutionId = plaidInstitutionId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getPlaidInstitutionId() {
        return plaidInstitutionId;
    }

    public void setPlaidInstitutionId(String plaidInstitutionId) {
        this.plaidInstitutionId = plaidInstitutionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

