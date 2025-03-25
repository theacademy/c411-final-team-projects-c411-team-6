package org.mthree.dao;

import org.mthree.dao.mappers.AssetMapper;
import org.mthree.dao.mappers.UserMapper;
import org.mthree.dto.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbc;

    public UserDaoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public User createNewUser(User user) {
        final String INSERT_USER = "INSERT INTO users(username, password) VALUES(?, ?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());

            return statement;
        }, keyHolder);

        user.setId(keyHolder.getKey().intValue());

        return user;
    }

    @Override
    public List<User> getAllUsers() {
        final String SELECT_ALL_USERS = "SELECT * FROM users";
        return jdbc.query(SELECT_ALL_USERS, new UserMapper());
    }

    @Override
    public User findUserById(int id) {
        try {
            final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
            return jdbc.queryForObject(SELECT_USER_BY_ID, new UserMapper(), id);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public void updateUser(User user) {
        final String UPDATE_USER = "UPDATE users SET username = ?, password = ? WHERE id = ?";

        jdbc.update(UPDATE_USER,
                user.getUsername(),
                user.getPassword(),
                user.getId());
    }

    @Override
    public void deleteUser(int id) {
        final String DELETE_USER = "DELETE FROM users WHERE id = ?";
        jdbc.update(DELETE_USER, id);
    }
}
