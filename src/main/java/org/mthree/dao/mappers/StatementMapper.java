package org.mthree.dao.mappers;

import org.mthree.dto.Statement;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatementMapper implements RowMapper<Statement> {

    @Override
    public Statement mapRow(ResultSet rs, int rowNum) throws SQLException {
        Statement statement = new Statement();
        statement.setId(rs.getInt("id"));
        statement.setUserId(rs.getInt("user_id"));
        statement.setMonth(rs.getInt("month"));
        statement.setYear(rs.getInt("year"));
        statement.setTotalIncome(rs.getBigDecimal("total_income"));
        statement.setTotalExpenses(rs.getBigDecimal("total_expenses"));
        statement.setNetCashFlow(rs.getBigDecimal("net_cash_flow"));
        return statement;
    }
}

