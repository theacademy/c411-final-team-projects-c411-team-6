package org.mthree.service;

import com.plaid.client.model.SandboxPublicTokenCreateRequest;
import com.plaid.client.request.PlaidApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mthree.dao.ItemDao;
import org.mthree.dao.ItemDaoImpl;
import org.mthree.dao.UserDao;
import org.mthree.dto.Item;
import org.mthree.dto.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemServiceTest {

    @Mock
    private ItemDao itemDao;

    @Mock
    private PlaidApi plaidApi;

    @InjectMocks
    private ItemService itemService;

    private Item sampleItem;
    private SandboxPublicTokenCreateRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleItem = new Item("plaidAccessToken", "plaidItemId", LocalDateTime.now());
        sampleItem.setUserId(1);
        sampleItem.setId(1);


    }

    @Test
    void testAddPlaidItem() throws IOException {
        // Arrange

        // Act
        itemService.addPlaidItem(sampleItem);

        // Assert
        assertNotNull(itemDao);
    }

    @Test
    void testGetPlaidItem() throws IOException {
        // Arrange
        when(itemDao.findItemsByUserId("1")).thenReturn(List.of(sampleItem));

        // Act
        List<Item> result = itemService.getItemsById("1");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.get(0).getUserId());


    }


}