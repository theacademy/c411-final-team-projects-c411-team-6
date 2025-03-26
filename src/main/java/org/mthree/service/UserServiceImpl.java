package org.mthree.service;

import org.mthree.dao.UserDao;
import org.mthree.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

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
            return userDao.findUserById(id);
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
        User existing = userDao.findUserById(id);
        if (existing == null) {
            user.setUsername("User not found, update failed");
            return user;
        }
        userDao.updateUser(user);
        return user;
    }

    @Override
    public void deleteUserById(int id) {
        User user = userDao.findUserById(id);
        if (user != null) {
            userDao.deleteUser(id);
            System.out.println("User ID: " + id + " deleted");
        } else {
            System.out.println("User ID: " + id + " not found, deletion skipped");
        }
    }
}
