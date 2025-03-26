package org.mthree.service;

import org.mthree.dao.UserDao;
import org.mthree.dto.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class UserService {

    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;

    public UserService(JdbcTemplate jdbcTemplate, UserDao userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
    }


    public User createUser(String username, String password) {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        int userId = userDao.saveUser(username, password);  // Save user and password
        return new User(userId, username, password, currentTime, currentTime);  // Return user with generated userId
    }

    public User checkUserCredentials(String username, String password) {
        User user = userDao.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user; // Return user if username and password match
        }
        return null;
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

    public int deleteUserById(int id) {
        return userDao.deleteUserById(id);
    }

    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }
}

