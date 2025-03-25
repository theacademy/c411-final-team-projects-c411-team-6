package org.mthree.controllers;

import org.mthree.dao.UserDao;
import org.mthree.dto.User;
import org.mthree.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User createdUser = userService.createUser(user.getUsername(), user.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);  // Return created user
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        User user = userService.getUserById(id);
        return (user != null) ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        int result = userService.deleteUserById(id);
        return (result > 0) ? ResponseEntity.ok("User deleted!")
                : ResponseEntity.badRequest().body("User not found.");
    }
}
