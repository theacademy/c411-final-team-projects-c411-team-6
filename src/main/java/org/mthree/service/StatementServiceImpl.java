package org.mthree.service;

import org.mthree.dao.StatementDao;
import org.mthree.dto.Statement;

import org.mthree.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class StatementServiceImpl implements StatementService{

    @Autowired
    StatementDao statementDao;

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
}
