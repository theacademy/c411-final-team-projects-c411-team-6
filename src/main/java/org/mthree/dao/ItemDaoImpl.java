package org.mthree.dao;

import org.mthree.dao.mappers.AssetMapper;
import org.mthree.dto.Item;
import org.springframework.jdbc.core.JdbcTemplate;
import org.mthree.dao.mappers.ItemMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemDaoImpl implements ItemDao {
    private final JdbcTemplate jdbcTemplate;

    public ItemDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addItem(Item item) {
        String sql = "INSERT INTO items(user_id, plaid_access_token, plaid_item_id, created_at) VALUES (?, ?, ?, ?)";
        System.out.println("In DAO " + item);
        jdbcTemplate.update(sql,
                item.getUserId(),
                item.getPlaidAccessToken(),
                item.getPlaidItemId(),
                item.getCreatedAt()
        );
        System.out.println("ADDED ITEM: " + item);
    }

    @Override
    public List<Item> findItemsByUserId(String userId){
        System.out.println("IN DAO... Getting items");
        String sql = "SELECT * FROM items WHERE user_id = ?";
        return jdbcTemplate.query(sql, new ItemMapper(), userId);
    }

}
