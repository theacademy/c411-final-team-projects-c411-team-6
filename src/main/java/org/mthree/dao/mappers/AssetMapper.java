package org.mthree.dao.mappers;

import org.mthree.dto.Asset;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AssetMapper implements RowMapper<Asset> {

    @Override
    public Asset mapRow(ResultSet rs, int rowNum) throws SQLException {

        Asset at = new Asset();
        at.setId(rs.getInt("id"));
        at.setUserId(rs.getInt("user_id"));
        at.setValue(rs.getBigDecimal("value"));
        at.setDescription(rs.getString("description"));

        return at;
    }

}
