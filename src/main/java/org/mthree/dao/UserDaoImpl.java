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
import org.springframework.jdbc.core.RowMapper;



@Repository
public class UserDaoImpl implements UserDao{
    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper to map SQL rows to Java objects
    private RowMapper<User> userRowMapper = (rs, rowNum) -> new User(
            rs.getInt("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("created_at"),
            rs.getString("updated_at")
    );

  @Override
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, userRowMapper, username);
        } catch (Exception e) {
            return null; // If no user is found, return null
        }
    }

  @Override
    public int saveUser(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        return jdbcTemplate.update(sql, username, password);
    }
    
  @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, userRowMapper);
    }
    
    @Override
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, userRowMapper, id);
    }

    @Override
    public void deleteUser(int id) {
        final String DELETE_USER = "DELETE FROM users WHERE id = ?";
        jdbc.update(DELETE_USER, id);
      
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
    public void updateUser(User user) {
        final String UPDATE_USER = "UPDATE users SET username = ?, password = ? WHERE id = ?";

        jdbc.update(UPDATE_USER,
                user.getUsername(),
                user.getPassword(),
                user.getId());
    }
    
}
