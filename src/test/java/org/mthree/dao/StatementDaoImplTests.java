package org.mthree.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mthree.dao.mappers.StatementMapper;
import org.mthree.dto.Statement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StatementDaoImplTests {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private StatementDaoImpl statementDao;

    @BeforeEach
    public void setUp() {
    }

    @Test
    @DisplayName("Create New Statement Test")
    public void createNewStatementTest() {
        Statement statement = new Statement();
        statement.setUserId(1);
        statement.setMonth(3);
        statement.setYear(2025);
        statement.setTotalIncome(BigDecimal.valueOf(1000));
        statement.setTotalExpenses(BigDecimal.valueOf(700));

        when(jdbcTemplate.update(any(PreparedStatementCreator.class), any(KeyHolder.class))).thenAnswer(invocation -> {
            KeyHolder keyHolder = invocation.getArgument(1);
            keyHolder.getKeyList().add(Map.of("id", 1));
            return 1;
        });

        Statement result = statementDao.createNewStatement(statement);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(BigDecimal.valueOf(300), result.getNetCashFlow());
        verify(jdbcTemplate, times(1)).update(any(PreparedStatementCreator.class), any(KeyHolder.class));
    }

    @Test
    @DisplayName("Get All Statements Test")
    public void getAllStatementsTest() {
        when(jdbcTemplate.query(eq("SELECT * FROM statements"), any(StatementMapper.class))).thenReturn(Arrays.asList());

        List<Statement> result = statementDao.getAllStatements();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(jdbcTemplate, times(1)).query(eq("SELECT * FROM statements"), any(StatementMapper.class));
    }

    @Test
    @DisplayName("Find Statement By ID Test")
    public void findStatementByIdTest() {
        Statement statement = new Statement();
        statement.setId(1);
        statement.setUserId(1);
        statement.setMonth(4);
        statement.setYear(2025);
        statement.setTotalIncome(BigDecimal.valueOf(1200));
        statement.setTotalExpenses(BigDecimal.valueOf(900));
        statement.setNetCashFlow(BigDecimal.valueOf(300));

        when(jdbcTemplate.queryForObject(
                eq("SELECT * FROM statements WHERE id = ?"),
                any(StatementMapper.class),
                eq(1)
        )).thenReturn(statement);

        Statement result = statementDao.findStatementById(1);

        assertNotNull(result);
        assertEquals(4, result.getMonth());
        verify(jdbcTemplate, times(1)).queryForObject(
                eq("SELECT * FROM statements WHERE id = ?"),
                any(StatementMapper.class),
                eq(1)
        );
    }

    @Test
    @DisplayName("Update Statement Test")
    public void updateStatementTest() {
        Statement statement = new Statement();
        statement.setId(1);
        statement.setUserId(1);
        statement.setMonth(5);
        statement.setYear(2025);
        statement.setTotalIncome(BigDecimal.valueOf(1500));
        statement.setTotalExpenses(BigDecimal.valueOf(1000));
        statement.setNetCashFlow(BigDecimal.valueOf(500));

        when(jdbcTemplate.update(
                eq("UPDATE statements SET user_id = ?, month = ?, year = ?, total_income = ?, total_expenses = ?, net_cash_flow = ? WHERE id = ?"),
                eq(1),
                eq(5),
                eq(2025),
                eq(BigDecimal.valueOf(1500)),
                eq(BigDecimal.valueOf(1100)),
                eq(BigDecimal.valueOf(400)),
                eq(1)
        )).thenReturn(1);

        statement.setTotalExpenses(BigDecimal.valueOf(1100));
        statement.setNetCashFlow(BigDecimal.valueOf(400));
        statementDao.updateStatement(statement);

        verify(jdbcTemplate, times(1)).update(
                eq("UPDATE statements SET user_id = ?, month = ?, year = ?, total_income = ?, total_expenses = ?, net_cash_flow = ? WHERE id = ?"),
                eq(1),
                eq(5),
                eq(2025),
                eq(BigDecimal.valueOf(1500)),
                eq(BigDecimal.valueOf(1100)),
                eq(BigDecimal.valueOf(400)),
                eq(1)
        );
    }

    @Test
    @DisplayName("Delete Statement Test")
    public void deleteStatementTest() {
        when(jdbcTemplate.update(
                eq("DELETE FROM statement WHERE id = ?"),
                eq(1)
        )).thenReturn(1);

        statementDao.deleteStatement(1);

        verify(jdbcTemplate, times(1)).update(
                eq("DELETE FROM statement WHERE id = ?"),
                eq(1)
        );
    }
}