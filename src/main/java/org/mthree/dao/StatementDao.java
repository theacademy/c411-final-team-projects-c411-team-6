package org.mthree.dao;

import org.mthree.dto.Statement;
import java.util.List;

public interface StatementDao {

    Statement createNewStatement(Statement statement);

    List<Statement> getAllStatements();

    Statement findStatementById(int id);

    void updateStatement(Statement statement);

    void deleteStatement(int id);
}