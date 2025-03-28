package org.mthree.dao;


import org.mthree.dao.mappers.AssetMapper;
import org.mthree.dao.mappers.StatementMapper;
import org.mthree.dto.Statement;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class StatementDaoImpl implements StatementDao {

    private final JdbcTemplate jdbc;

    public StatementDaoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Statement createNewStatement(Statement statement) {

        final String INSERT_STATEMENT = "INSERT INTO statements(user_id, month, year, total_income, total_expenses) VALUES(?, ?, ?, ?, ?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement prepared = connection.prepareStatement(INSERT_STATEMENT, java.sql.Statement.RETURN_GENERATED_KEYS);

            prepared.setInt(1, statement.getUserId());
            prepared.setInt(2, statement.getMonth());
            prepared.setInt(3, statement.getYear());
            prepared.setBigDecimal(4, statement.getTotalIncome());
            prepared.setBigDecimal(5, statement.getTotalExpenses());


            return prepared;
        }, keyHolder);

        statement.setId(keyHolder.getKey().intValue());
        statement.setNetCashFlow(statement.getTotalIncome().subtract(statement.getTotalExpenses()));

        return statement;
    }

    @Override
    public List<Statement> getAllStatements() {
        final String SELECT_ALL_STATEMENTS = "SELECT * FROM statements";
        return jdbc.query(SELECT_ALL_STATEMENTS, new StatementMapper());
    }

    @Override
    public Statement findStatementById(int id) {
        try {
            final String SELECT_STATEMENT_BY_ID = "SELECT * FROM statements WHERE id = ?";
            return jdbc.queryForObject(SELECT_STATEMENT_BY_ID, new StatementMapper(), id);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public void updateStatement(Statement statement) {

        final String UPDATE_STATEMENT = "UPDATE statements SET user_id = ?, month = ?, year = ?, total_income = ?, total_expenses = ?, net_cash_flow = ? WHERE id = ?";

        jdbc.update(UPDATE_STATEMENT,
                statement.getUserId(),
                statement.getMonth(),
                statement.getYear(),
                statement.getTotalIncome(),
                statement.getTotalExpenses(),
                statement.getNetCashFlow(),
                statement.getId());

    }

    @Override
    public void deleteStatement(int id) {

        final String DELETE_STATEMENT = "DELETE FROM statement WHERE id = ?";
        jdbc.update(DELETE_STATEMENT, id);

    }
}

