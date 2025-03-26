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
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user.getUsername(), user.getPassword());
            // Debugging log for successful user creation
            System.out.println("User created successfully: " + createdUser.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User creation failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        boolean isValidUser = userService.checkUserCredentials(user.getUsername(), user.getPassword());
        if (isValidUser) {
            // For now, return a success message
            return ResponseEntity.ok("Login successful!");
        } else {
            // Return error message if username/password is invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
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
