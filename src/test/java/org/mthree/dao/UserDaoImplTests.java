package org.mthree.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mthree.dto.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserDaoImplTests {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private UserDaoImpl userDao;

    @BeforeEach
    public void setUp() {
    }

    @Test
    @DisplayName("Create New User Test")
    public void createNewUserTest() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");

        when(jdbcTemplate.update(any(PreparedStatementCreator.class), any(KeyHolder.class))).thenAnswer(invocation -> {
            KeyHolder keyHolder = invocation.getArgument(1);
            keyHolder.getKeyList().add(Map.of("id", 1));
            return 1;
        });

        User result = userDao.createNewUser(user);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(jdbcTemplate, times(1)).update(any(PreparedStatementCreator.class), any(KeyHolder.class));
    }

    @Test
    @DisplayName("Get All Users Test")
    public void getAllUsersTest() {
        when(jdbcTemplate.query(eq("SELECT * FROM users"), any(RowMapper.class))).thenReturn(Arrays.asList());

        List<User> result = userDao.getAllUsers();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(jdbcTemplate, times(1)).query(eq("SELECT * FROM users"), any(RowMapper.class));
    }

    @Test
    @DisplayName("Get User By Username Test")
    public void getUserByUsernameTest() {
        User user = new User(1, "johndoe", "pass456", "2023-01-01", "2023-01-01");

        when(jdbcTemplate.queryForObject(
                eq("SELECT * FROM users WHERE username = ?"),
                any(RowMapper.class),
                eq("johndoe")
        )).thenReturn(user);

        User result = userDao.getUserByUsername("johndoe");

        assertNotNull(result);
        assertEquals("johndoe", result.getUsername());
        verify(jdbcTemplate, times(1)).queryForObject(
                eq("SELECT * FROM users WHERE username = ?"),
                any(RowMapper.class),
                eq("johndoe")
        );
    }

    @Test
    @DisplayName("Get User By ID Test")
    public void getUserByIdTest() {
        User user = new User(1, "janedoe", "pass789", "2023-01-01", "2023-01-01");

        when(jdbcTemplate.queryForObject(
                eq("SELECT * FROM users WHERE id = ?"),
                any(RowMapper.class),
                eq(1)
        )).thenReturn(user);

        User result = userDao.getUserById(1);

        assertNotNull(result);
        assertEquals("janedoe", result.getUsername());
        verify(jdbcTemplate, times(1)).queryForObject(
                eq("SELECT * FROM users WHERE id = ?"),
                any(RowMapper.class),
                eq(1)
        );
    }

    @Test
    @DisplayName("Update User Test")
    public void updateUserTest() {
        User user = new User(1, "olduser", "oldpass", "2023-01-01", "2023-01-01");

        when(jdbcTemplate.update(
                eq("UPDATE users SET username = ?, password = ? WHERE id = ?"),
                eq("newuser"),
                eq("oldpass"),
                eq(1)
        )).thenReturn(1);

        user.setUsername("newuser");
        userDao.updateUser(user);

        verify(jdbcTemplate, times(1)).update(
                eq("UPDATE users SET username = ?, password = ? WHERE id = ?"),
                eq("newuser"),
                eq("oldpass"),
                eq(1)
        );
    }

    @Test
    @DisplayName("Delete User Test")
    public void deleteUserTest() {
        when(jdbcTemplate.update(
                eq("DELETE FROM users WHERE id = ?"),
                eq(1)
        )).thenReturn(1);

        userDao.deleteUser(1);

        verify(jdbcTemplate, times(1)).update(
                eq("DELETE FROM users WHERE id = ?"),
                eq(1)
        );
    }

    @Test
    @DisplayName("Save User Test")
    public void saveUserTest() {
        when(jdbcTemplate.update(
                eq("INSERT INTO users (username, password) VALUES (?, ?)"),
                eq("simpleuser"),
                eq("simplepass")
        )).thenReturn(1);

        int rowsAffected = userDao.saveUser("simpleuser", "simplepass");

        assertEquals(1, rowsAffected);
        verify(jdbcTemplate, times(1)).update(
                eq("INSERT INTO users (username, password) VALUES (?, ?)"),
                eq("simpleuser"),
                eq("simplepass")
        );
    }
}