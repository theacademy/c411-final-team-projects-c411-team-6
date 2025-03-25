package org.mthree.controllers;

import org.mthree.dao.UserDao;
import org.mthree.dto.User;
import org.mthree.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestParam String username) {
        int result = userDao.saveUser(username);
        return (result > 0) ? ResponseEntity.ok("User created!")
                : ResponseEntity.badRequest().body("Failed to create user.");
    }

    @GetMapping
    public List<User> getUsers() {
        return userDao.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        User user = userDao.getUserById(id);
        return (user != null) ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        int result = userDao.deleteUserById(id);
        return (result > 0) ? ResponseEntity.ok("User deleted!")
                : ResponseEntity.badRequest().body("User not found.");
    }
}
