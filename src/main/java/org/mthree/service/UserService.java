package org.mthree.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class UserService {

    private final JdbcTemplate jdbcTemplate;

    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int saveUser(String username) {
        String sql = "INSERT INTO users (username) VALUES (?)";
        return jdbcTemplate.update(sql, username);
    }

    public List<String> getAllUsers() {
        String sql = "SELECT username FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("username"));
    }
}

