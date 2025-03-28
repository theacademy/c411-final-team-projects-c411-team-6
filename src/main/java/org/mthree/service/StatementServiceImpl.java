package org.mthree.service;

import org.mthree.dao.StatementDao;
import org.mthree.dto.Statement;

import org.mthree.dto.Transaction;
import org.mthree.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatementServiceImpl implements StatementService{

    @Autowired
    StatementDao statementDao;

    @Autowired
    TransactionService transactionService;

    public StatementServiceImpl(StatementDao statementDao) {
        this.statementDao = statementDao;
    }

    @Override
    public List<Statement> getAllStatements() {
        return statementDao.getAllStatements();
    }

    @Override
    public Statement getStatementById(int id) {
        try {
            return statementDao.findStatementById(id);
        } catch (DataAccessException ex) {
            System.out.println("Statement Not Found");
            return null;
        }
    }

    @Override
    public Map<String, BigDecimal> createStatement(int userId, LocalDate startDate, LocalDate endDate) {

        List<Transaction> transactions = transactionService.getTransactionsByDate((long)userId, startDate, endDate);

        List<Transaction>  revenues = transactionService.getRevenueTransactions(transactions);
        List<Transaction>  expenses = transactionService.getExpenseTransactions(transactions);

        Map<String, BigDecimal> statementInfo = calculateStatementInfo(revenues, expenses);

        Statement statement = new Statement();
        statement.setUserId(userId);
        statement.setMonth(endDate.getMonthValue());
        statement.setYear(endDate.getYear());
        statement.setTotalIncome(statementInfo.getOrDefault("Total_Revenue", BigDecimal.ZERO));
        statement.setTotalExpenses(statementInfo.getOrDefault("Total_Expense", BigDecimal.ZERO));
        statement.setNetCashFlow(statementInfo.getOrDefault("Net_Cash_Flow", BigDecimal.ZERO));

        statementDao.createNewStatement(statement);

        return statementInfo;

    }

    @Override
    public Statement addNewStatement(Statement statement) {
        if (statement == null) {

            return null;
        }
        if (statement.getYear() < 0 || statement.getMonth() < 1 || statement.getMonth() > 12 ||
                statement.getTotalIncome() == null || statement.getTotalIncome().compareTo(BigDecimal.ZERO) < 0 ||
                statement.getTotalExpenses() == null || statement.getTotalExpenses().compareTo(BigDecimal.ZERO) < 0
        ) {
            System.out.println("Invalid input: statement information missing/invalid");

            return null;
        }
        return statementDao.createNewStatement(statement);
    }

    @Override
    public Statement updateStatementData(int id, Statement statement) {
        if (statement == null) {
            return null;
        }
        if (id != statement.getId()) {
            System.out.println("IDs do not match, statement not updated");
            return null;
        }
        Statement existing = statementDao.findStatementById(id);
        if (existing == null) {
            System.out.println("Statement not found, update failed");
            return null;
        }
        statementDao.updateStatement(statement);
        return statement;
    }

    @Override
    public void deleteStatementById(int id) {
        Statement statement = statementDao.findStatementById(id);
        if (statement != null) {
            statementDao.deleteStatement(id);
            System.out.println("Statement ID: " + id + " deleted");
        } else {
            System.out.println("Statement ID: " + id + " not found, deletion skipped");
        }
    }


    public Map<String, BigDecimal> calculateStatementInfo(List<Transaction> revenue,
                                                          List<Transaction> expense) {

        Map<String, BigDecimal> statementInfo = new HashMap<>();

        statementInfo.put("Net_Cash_Flow", BigDecimal.ZERO);
        statementInfo.put("Total_Revenue",  BigDecimal.ZERO);
        statementInfo.put("Food_Sales",  BigDecimal.ZERO);
        statementInfo.put("Service_Sales",  BigDecimal.ZERO);
        statementInfo.put("Goods_Sales",  BigDecimal.ZERO);
        statementInfo.put("Other_Sales",  BigDecimal.ZERO);
        statementInfo.put("Total_Expense",  BigDecimal.ZERO);
        statementInfo.put("Travel_Costs",  BigDecimal.ZERO);
        statementInfo.put("Inventory_Costs",  BigDecimal.ZERO);
        statementInfo.put("Labor_Costs",  BigDecimal.ZERO);


        for (Transaction txn : revenue) {
            String category = txn.getCategory();
            String amountString = txn.getAmount().toString();
            BigDecimal amount = new BigDecimal(amountString);

            if (category.equalsIgnoreCase("Food and Drink")) {
                statementInfo.put("Food_Sales", statementInfo.getOrDefault("Food_Sales", BigDecimal.ZERO).add(amount).abs());
            }
            else if (category.equalsIgnoreCase("Service")) {
                statementInfo.put("Service_Sales", statementInfo.getOrDefault("Service_Sales", BigDecimal.ZERO).add(amount).abs());
            }
            else if (category.equalsIgnoreCase("Transfer")) {
                statementInfo.put("Goods_Sales", statementInfo.getOrDefault("Goods_Sales", BigDecimal.ZERO).add(amount).abs());
            }
            else {
                statementInfo.put("Other_Sales", statementInfo.getOrDefault("Other_Sales", BigDecimal.ZERO).add(amount).abs());
            }

        }

        for (Transaction txn : expense) {
            String category = txn.getCategory();
            String amountString = txn.getAmount().toString();
            BigDecimal amount = new BigDecimal(amountString);

            if (category.equalsIgnoreCase("Payment")) {
                statementInfo.put("Labor_Costs", statementInfo.getOrDefault("Labor_Costs", BigDecimal.ZERO).add(amount).abs());
            }
            else if (category.equalsIgnoreCase("Travel")) {
                statementInfo.put("Travel_Costs", statementInfo.getOrDefault("Travel_Costs", BigDecimal.ZERO).add(amount).abs());
            }
            else {
                statementInfo.put("Inventory_Costs", statementInfo.getOrDefault("Inventory_Costs", BigDecimal.ZERO).add(amount).abs());
            }

        }

        BigDecimal revenueTotal = statementInfo.getOrDefault("Food_Sales", BigDecimal.ZERO)
                .add(statementInfo.getOrDefault("Service_Sales", BigDecimal.ZERO))
                .add(statementInfo.getOrDefault("Goods_Sales", BigDecimal.ZERO))
                .add(statementInfo.getOrDefault("Other_Sales", BigDecimal.ZERO)).abs();


        BigDecimal expenseTotal = statementInfo.getOrDefault("Labor_Costs", BigDecimal.ZERO)
                .add(statementInfo.getOrDefault("Travel_Costs", BigDecimal.ZERO))
                .add(statementInfo.getOrDefault("Inventory_Costs", BigDecimal.ZERO)).abs();

        BigDecimal netCashFlow = revenueTotal.subtract(expenseTotal);

        statementInfo.put("Total_Revenue", revenueTotal);
        statementInfo.put("Total_Expense", expenseTotal);
        statementInfo.put("Net_Cash_Flow", netCashFlow);


        return statementInfo;

    }

}
