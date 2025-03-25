package org.mthree.controllers;

import org.mthree.dao.StatementDao;
import org.mthree.dto.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statements")
public class StatementController {

    @Autowired
    private StatementDao statementDao;

    @PostMapping
    public ResponseEntity<Statement> createStatement(@RequestBody Statement statement) {
        Statement created = statementDao.createNewStatement(statement);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
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