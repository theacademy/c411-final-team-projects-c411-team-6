package org.mthree.dao;

import org.mthree.dto.User;

import java.util.List;

public interface UserDao {
    int saveUser(String username, String password);
    List<User> getAllUsers();
    User getUserById(int id);
    int deleteUserById(int id);
}
