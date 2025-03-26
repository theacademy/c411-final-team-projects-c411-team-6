package org.mthree.service;

import org.mthree.dao.UserDao;
import org.mthree.dto.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public User getUserById(int id) {
        try {
            return userDao.getUserById(id);
        } catch (DataAccessException ex) {
            User user = new User();
            user.setUsername("User Not Found");
            user.setPassword(null);
            return user;
        }
    }

    @Override
    public User addNewUser(User user) {
        if (user == null) {
            return null;
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty() || user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            user.setUsername("Invalid input: username, or password missing/invalid");
            user.setPassword(null);
            return user;
        }
        return userDao.createNewUser(user);
    }

    @Override
    public User updateUserData(int id, User user) {
        if (user == null) {
            return null;
        }
        if (id != user.getId()) {
            user.setUsername("IDs do not match, user not updated");
            return user;
        }
        User existing = userDao.getUserById(id);
        if (existing == null) {
            user.setUsername("User not found, update failed");
            return user;
        }
        userDao.updateUser(user);
        return user;
    }

    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    @Override
    public int deleteUserById(int id) {
        return userDao.deleteUser(id);
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

}
