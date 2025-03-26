package org.mthree.dao;

import org.mthree.dao.mappers.ItemMapper;
import org.mthree.dto.Item;

import java.util.List;

public interface ItemDao {
    void addItem(Item item);

    List<Item> findItemsByUserId(String userId);
}
