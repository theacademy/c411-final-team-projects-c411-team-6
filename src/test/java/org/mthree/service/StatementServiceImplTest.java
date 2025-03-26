package org.mthree.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mthree.dao.StatementDao;
import org.mthree.dto.Statement;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

class StatementServiceImplTest {

    @Mock
    private StatementDao statementDao;

    @InjectMocks
    private StatementServiceImpl statementService;

    private Statement sampleStatement;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        sampleStatement = new Statement(1, 2025, 5, BigDecimal.valueOf(5000), BigDecimal.valueOf(2000));
    }

    // Test getAllStatements method
    @Test
    void testGetAllStatements() {
        Statement statement2 = new Statement(2, 2025, 6, BigDecimal.valueOf(6000), BigDecimal.valueOf(2500));
        when(statementDao.getAllStatements()).thenReturn(Arrays.asList(sampleStatement, statement2));

        var result = statementService.getAllStatements();

        assertEquals(2, result.size());
        assertTrue(result.contains(sampleStatement));
        assertTrue(result.contains(statement2));
    }

    // Test getStatementById when statement is found
    @Test
    void testGetStatementById_Success() {
        when(statementDao.findStatementById(1)).thenReturn(sampleStatement);

        Statement result = statementService.getStatementById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(BigDecimal.valueOf(5000), result.getTotalIncome());
    }

    // Test getStatementById when statement is not found
    @Test
    void testGetStatementById_NotFound() {
        when(statementDao.findStatementById(1)).thenReturn(null);

        Statement result = statementService.getStatementById(1);

        assertNull(result);
    }

    // Test addNewStatement with valid input
    @Test
    void testAddNewStatement_Success() {
        Statement newStatement = new Statement(0, 2025, 7, BigDecimal.valueOf(7000), BigDecimal.valueOf(3000));
        when(statementDao.createNewStatement(any(Statement.class))).thenReturn(newStatement);

        Statement result = statementService.addNewStatement(newStatement);

        assertNotNull(result);
        assertEquals(0, result.getId()); // The ID should remain 0 as mocked
        verify(statementDao, times(1)).createNewStatement(newStatement);
    }


    // Test addNewStatement with invalid input
    @Test
    void testAddNewStatement_InvalidInput() {
        Statement invalidStatement = new Statement(0, 2025, 7, BigDecimal.valueOf(-100), BigDecimal.valueOf(500));

        Statement result = statementService.addNewStatement(invalidStatement);

        assertNull(result);
    }

    // Test updateStatementData when statement is updated successfully
    @Test
    void testUpdateStatementData_Success() {
        Statement sampleStatement = new Statement(1, 2025, 4, BigDecimal.valueOf(5000), BigDecimal.valueOf(2000));
        when(statementDao.findStatementById(1)).thenReturn(sampleStatement);
        doNothing().when(statementDao).updateStatement(any(Statement.class)); // Mock void method with doNothing()

        Statement updatedStatement = new Statement(1, 2025, 5, BigDecimal.valueOf(6000), BigDecimal.valueOf(2500));


        Statement result = statementService.updateStatementData(1, updatedStatement);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(BigDecimal.valueOf(6000), result.getTotalIncome());

        verify(statementDao, times(1)).updateStatement(updatedStatement);
    }


    // Test updateStatementData when statement ID does not match
    @Test
    void testUpdateStatementData_IDMismatch() {
        Statement updatedStatement = new Statement(2, 2025, 5, BigDecimal.valueOf(6000), BigDecimal.valueOf(2500));

        Statement result = statementService.updateStatementData(1, updatedStatement);

        assertNull(result);
    }

    // Test deleteStatementById when statement exists
    @Test
    void testDeleteStatementById_Success() {
        when(statementDao.findStatementById(1)).thenReturn(sampleStatement);
        doNothing().when(statementDao).deleteStatement(1);

        statementService.deleteStatementById(1);

        verify(statementDao, times(1)).deleteStatement(1);
    }

    // Test deleteStatementById when statement does not exist
    @Test
    void testDeleteStatementById_NotFound() {
        when(statementDao.findStatementById(1)).thenReturn(null);

        statementService.deleteStatementById(1);

        verify(statementDao, times(0)).deleteStatement(1);
    }
}
