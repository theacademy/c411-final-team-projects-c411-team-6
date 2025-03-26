package org.mthree.service;

import org.mthree.dto.Statement;


import java.util.List;

public interface StatementService {

    List<Statement> getAllStatements();

    Statement getStatementById(int id);

    Statement addNewStatement(Statement statement);

    Statement updateStatementData(int id, Statement statement);

    void deleteStatementById(int id);

}
