package org.mthree.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mthree.dao.UserDao;
import org.mthree.dto.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleUser = new User(1, "john_doe", "password123", "2023-03-26T12:30:00", "2023-03-26T12:30:00");
    }

    @Test
    void testGetUserById_Success() {
        // Arrange
        when(userDao.getUserById(1)).thenReturn(sampleUser);

        // Act
        User result = userService.getUserById(1);

        // Assert
        assertNotNull(result);
        assertEquals("john_doe", result.getUsername());
        verify(userDao, times(1)).getUserById(1);
    }

    @Test
    void testGetUserById_UserNotFound() {
        // Arrange
        when(userDao.getUserById(999)).thenThrow(new EmptyResultDataAccessException(1));

        // Act
        User result = userService.getUserById(999);

        // Assert
        assertNotNull(result);
        assertEquals("User Not Found", result.getUsername());
        assertNull(result.getPassword());
        verify(userDao, times(1)).getUserById(999);
    }

    @Test
    void testAddNewUser_Success() {
        // Arrange
        User newUser = new User(0, "john_doe", "password123", null, null);
        when(userDao.createNewUser(newUser)).thenReturn(newUser);

        // Act
        User result = userService.addNewUser(newUser);

        // Assert
        assertNotNull(result);
        assertEquals("john_doe", result.getUsername());
        verify(userDao, times(1)).createNewUser(newUser);
    }

    @Test
    void testAddNewUser_InvalidInput() {
        // Arrange
        User invalidUser = new User(0, "", "", null, null);

        // Act
        User result = userService.addNewUser(invalidUser);

        // Assert
        assertNotNull(result);
        assertEquals("Invalid input: username, or password missing/invalid", result.getUsername());
        assertNull(result.getPassword());
        verify(userDao, times(0)).createNewUser(invalidUser);
    }

    @Test
    void testUpdateUserData_Success() {
        // Arrange
        User updatedUser = new User(1, "john_doe_updated", "newpassword", "2023-03-26T13:00:00", "2023-03-26T13:00:00");
        when(userDao.getUserById(1)).thenReturn(sampleUser);
        doNothing().when(userDao).updateUser(updatedUser);

        // Act
        User result = userService.updateUserData(1, updatedUser);

        // Assert
        assertNotNull(result);
        assertEquals("john_doe_updated", result.getUsername());
        verify(userDao, times(1)).updateUser(updatedUser);
    }

    @Test
    void testUpdateUserData_IDMismatch() {
        // Arrange
        User updatedUser = new User(1, "john_doe_updated", "newpassword", "2023-03-26T13:00:00", "2023-03-26T13:00:00");

        // Act
        User result = userService.updateUserData(999, updatedUser);

        // Assert
        assertNotNull(result);
        assertEquals("IDs do not match, user not updated", result.getUsername());
        verify(userDao, times(0)).updateUser(updatedUser);
    }

    @Test
    void testDeleteUserById_Success() {
        // Arrange
        when(userDao.deleteUser(1)).thenReturn(1);

        // Act
        int result = userService.deleteUserById(1);

        // Assert
        assertEquals(1, result);
        verify(userDao, times(1)).deleteUser(1);
    }

    @Test
    void testDeleteUserById_Failure() {
        // Arrange
        when(userDao.deleteUser(999)).thenReturn(0);

        // Act
        int result = userService.deleteUserById(999);

        // Assert
        assertEquals(0, result);
        verify(userDao, times(1)).deleteUser(999);
    }

    @Test
    void testCreateUser() {
        // Arrange
        User newUser = new User(0, "john_doe", "password123", "2023-03-26T12:30:00", "2023-03-26T12:30:00");
        when(userDao.saveUser(anyString(), anyString())).thenReturn(1);

        // Act
        User result = userService.createUser("john_doe", "password123");

        // Assert
        assertNotNull(result);
        assertEquals("john_doe", result.getUsername());
        verify(userDao, times(1)).saveUser("john_doe", "password123");
    }

    @Test
    void testCheckUserCredentials_Success() {
        // Arrange
        User validUser = new User(1, "john_doe", "password123", "2023-03-26T12:30:00", "2023-03-26T12:30:00");
        when(userDao.getUserByUsername("john_doe")).thenReturn(validUser);

        // Act
        User result = userService.checkUserCredentials("john_doe", "password123");

        // Assert
        assertNotNull(result);
        assertEquals("john_doe", result.getUsername());
    }

    @Test
    void testCheckUserCredentials_Failure() {
        // Arrange
        when(userDao.getUserByUsername("john_doe")).thenReturn(null);

        // Act
        User result = userService.checkUserCredentials("john_doe", "wrongpassword");

        // Assert
        assertNull(result);
    }
}
