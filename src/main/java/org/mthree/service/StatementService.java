package org.mthree.service;

import org.mthree.dto.Statement;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface StatementService {

    List<Statement> getAllStatements();

    Statement getStatementById(int id);

    Statement addNewStatement(Statement statement);

    Statement updateStatementData(int id, Statement statement);

    void deleteStatementById(int id);

    Map<String, BigDecimal> createStatement(int userId, LocalDate startDate, LocalDate endDate);

}
