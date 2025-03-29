package org.mthree.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mthree.dao.mappers.ItemMapper;
import org.mthree.dto.Item;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ItemDaoImplTests {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ItemDaoImpl itemDao;

    @BeforeEach
    public void setUp() {
    }

    @Test
    @DisplayName("Add Item Test")
    public void addItemTest() {
        Item item = new Item();
        item.setUserId(1);
        item.setPlaidAccessToken("access-test-123");
        item.setPlaidItemId("item-test-456");
        item.setCreatedAt(LocalDateTime.now());

        when(jdbcTemplate.update(
                eq("INSERT INTO items(user_id, plaid_access_token, plaid_item_id, created_at) VALUES (?, ?, ?, ?)"),
                eq(1),
                eq("access-test-123"),
                eq("item-test-456"),
                any(LocalDateTime.class)
        )).thenReturn(1);

        itemDao.addItem(item);

        verify(jdbcTemplate, times(1)).update(
                eq("INSERT INTO items(user_id, plaid_access_token, plaid_item_id, created_at) VALUES (?, ?, ?, ?)"),
                eq(1),
                eq("access-test-123"),
                eq("item-test-456"),
                any(LocalDateTime.class)
        );
    }

    @Test
    @DisplayName("Find Items By User ID Test")
    public void findItemsByUserIdTest() {
        Item item = new Item();
        item.setUserId(1);
        item.setPlaidAccessToken("access-test-789");
        item.setPlaidItemId("item-test-101");
        item.setCreatedAt(LocalDateTime.now());
        List<Item> items = Arrays.asList(item);

        when(jdbcTemplate.query(
                eq("SELECT * FROM items WHERE user_id = ?"),
                any(ItemMapper.class),
                eq("1")
        )).thenReturn(items);

        List<Item> result = itemDao.findItemsByUserId("1");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("item-test-101", result.get(0).getPlaidItemId());
        verify(jdbcTemplate, times(1)).query(
                eq("SELECT * FROM items WHERE user_id = ?"),
                any(ItemMapper.class),
                eq("1")
        );
    }
}