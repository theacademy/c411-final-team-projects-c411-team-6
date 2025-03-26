package org.mthree.service;

import org.mthree.dto.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(int id);

    User addNewUser(User user);

    User updateUserData(int id, User user);

    int deleteUserById(int id);

    User getUserByUsername(String username);

    User createUser(String username, String password);

    User checkUserCredentials(String username, String password);


}

