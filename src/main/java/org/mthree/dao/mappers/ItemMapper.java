package org.mthree.dao.mappers;

import org.mthree.dto.Item;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemMapper implements RowMapper<Item> {
    @Override
    public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
        Item item = new Item();
        item.setId(rs.getLong("id"));
        item.setUserId(rs.getLong("user_id"));
        item.setPlaidAccessToken(rs.getString("plaid_access_token"));
        item.setPlaidItemId(rs.getString("plaid_item_id"));
        item.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return item;
    }
}
