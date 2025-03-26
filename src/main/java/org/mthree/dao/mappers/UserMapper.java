package org.mthree.dao.mappers;

import org.mthree.dto.User;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {

        User ur = new User();
        ur.setId(rs.getInt("id"));
        ur.setUsername(rs.getString("username"));
        ur.setPassword(rs.getString("password"));

        return ur;
    }

}
