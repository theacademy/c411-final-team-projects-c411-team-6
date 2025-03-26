package org.mthree.dao;

import org.mthree.dto.User;

import java.util.List;

public interface UserDao {

    User createNewUser(User user);

    List<User> getAllUsers();

    void updateUser(User user);

    void deleteUser(int id);
  
    int saveUser(String username, String password);
  
    User getUserById(int id);
  
    User getUserByUsername(String username);

}
