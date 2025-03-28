package org.mthree.controllers;

import org.mthree.dao.StatementDao;
import org.mthree.dto.Statement;
import org.mthree.service.StatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@RestController
@RequestMapping("/statements")
public class StatementController {

    @Autowired
    private StatementDao statementDao;

    @Autowired
    private StatementService statementService;

    @PostMapping
    public ResponseEntity<Statement> createStatement(@RequestBody Statement statement) {
        Statement created = statementDao.createNewStatement(statement);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/current-month")
    public ResponseEntity<Map<String, BigDecimal>> createCurrentStatement(@RequestParam int userId) {
        Map<String, BigDecimal> created = statementService.createStatement(userId, LocalDate.now().with(firstDayOfMonth()), LocalDate.now().with(lastDayOfMonth()));

        if (created == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(created);
    }

    @PostMapping("/by-month")
    public ResponseEntity<Map<String, BigDecimal>> createStatementByMonth(@RequestParam int userId, @RequestParam LocalDate date) {

        try {

            Map<String, BigDecimal> created = statementService.createStatement(userId, date.with(firstDayOfMonth()), date.with(lastDayOfMonth()));

            if (created == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.ok(created);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/by-date-range")
    public ResponseEntity<Map<String, BigDecimal>> createStatementDateRange(@RequestParam int userId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {

        try {

            Map<String, BigDecimal> created = statementService.createStatement(userId, startDate, endDate);

            if (created == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.ok(created);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping
    public List<Statement> getAllStatements() {
        return statementDao.getAllStatements();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Statement> getStatementById(@PathVariable int id) {
        Statement statement = statementDao.findStatementById(id);
        if (statement == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(statement);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Statement> updateStatement(@PathVariable int id, @RequestBody Statement statement) {
        if (id != statement.getId()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        Statement existing = statementDao.findStatementById(id);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        statementDao.updateStatement(statement);
        return ResponseEntity.ok(statement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatement(@PathVariable int id) {
        Statement statement = statementDao.findStatementById(id);
        if (statement == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        statementDao.deleteStatement(id);
        return ResponseEntity.noContent().build();
    }
}