package org.mthree.dao;

import org.mthree.dto.User;

import java.util.List;

public interface UserDao {

    User createNewUser(User user);

    List<User> getAllUsers();

    User findUserById(int id);

    void updateUser(User user);

    void deleteUser(int id);

}
