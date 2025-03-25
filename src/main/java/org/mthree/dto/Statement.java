package org.mthree.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Statement {

    private int id;
    private int user_id;
    private int month;
    private int year;
    private BigDecimal total_income;
    private BigDecimal total_expenses;
    private BigDecimal net_cash_flow;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public BigDecimal getTotal_income() {
        return total_income;
    }

    public void setTotal_income(BigDecimal total_income) {
        this.total_income = total_income;
    }

    public BigDecimal getTotal_expenses() {
        return total_expenses;
    }

    public void setTotal_expenses(BigDecimal total_expenses) {
        this.total_expenses = total_expenses;
    }

    public BigDecimal getNet_cash_flow() {
        return net_cash_flow;
    }

    public void setNet_cash_flow(BigDecimal net_cash_flow) {
        this.net_cash_flow = net_cash_flow;
    }


}
