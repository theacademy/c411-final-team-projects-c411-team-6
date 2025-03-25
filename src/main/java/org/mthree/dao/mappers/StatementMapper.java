package org.mthree.dao.mappers;

import org.mthree.dto.Statement;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatementMapper implements RowMapper<Statement> {

    @Override
    public Statement mapRow(ResultSet rs, int rowNum) throws SQLException {

        Statement st = new Statement();
        st.setId(rs.getInt("id"));
        st.setUserId(rs.getInt("user_id"));
        st.setMonth(rs.getInt("month"));
        st.setYear(rs.getInt("year"));
        st.setTotalIncome(rs.getBigDecimal("total_income"));
        st.setTotalExpenses(rs.getBigDecimal("total_expenses"));
        st.setNetCashFlow(rs.getBigDecimal("net_cash_flow"));

        return st;
    }

}
