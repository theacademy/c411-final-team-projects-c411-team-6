package org.mthree.dto;

import java.math.BigDecimal;

public class Asset {
    private int id;
    private int userId;
    private BigDecimal value;
    private String description;

    public Asset(){}

    public Asset(int i, int i1, String description, BigDecimal value) {
        this.id = i;
        this.userId = i1;
        this.value = value;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
