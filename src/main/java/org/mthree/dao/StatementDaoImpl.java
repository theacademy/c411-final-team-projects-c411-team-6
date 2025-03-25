package org.mthree.dao;

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
            PreparedStatement ps = connection.prepareStatement(INSERT_STATEMENT, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, statement.getUser_id());
            ps.setInt(2, statement.getMonth());
            ps.setInt(3, statement.getYear());
            ps.setBigDecimal(4, statement.getTotal_income());
            ps.setBigDecimal(5, statement.getTotal_expenses());
            return ps;
        }, keyHolder);

        statement.setId(keyHolder.getKey().intValue());
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
        final String UPDATE_STATEMENT = "UPDATE statements SET user_id = ?, month = ?, year = ?, total_income = ?, total_expenses = ? WHERE id = ?";

        jdbc.update(UPDATE_STATEMENT,
                statement.getUser_id(),
                statement.getMonth(),
                statement.getYear(),
                statement.getTotal_income(),
                statement.getTotal_expenses(),
                statement.getId());
    }

    @Override
    public void deleteStatement(int id) {
        final String DELETE_STATEMENT = "DELETE FROM statements WHERE id = ?";
        jdbc.update(DELETE_STATEMENT, id);
    }
}