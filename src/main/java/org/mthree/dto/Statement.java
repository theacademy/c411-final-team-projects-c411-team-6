package org.mthree.dto;

import java.math.BigDecimal;

public class Statement {

    private int id;
    private int userId;
    private int month;
    private int year;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netCashFlow;

    public Statement(int id, int userId, int month, BigDecimal totalIncome, BigDecimal totalExpenses) {
        this.id = id;
        this.userId = userId;
        this.month = month;
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
        this.netCashFlow = totalIncome.subtract(totalExpenses);
    }

    public Statement() {}


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

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(BigDecimal totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public BigDecimal getNetCashFlow() {
        return netCashFlow;
    }

    public void setNetCashFlow(BigDecimal netCashFlow) {
        this.netCashFlow = netCashFlow;
    }


}
