package org.mthree.service;

import org.mthree.dao.ItemDao;
import org.mthree.dto.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ItemService {
    @Autowired
    private final ItemDao itemDao;

    public ItemService(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    public void addPlaidItem(Item item){
        System.out.println("In SERVICE ADD PLAID ITEM Adding plaid item: " + item);
        item.setCreatedAt(LocalDateTime.now());
        itemDao.addItem(item);
    }

    public List<Item> getItemsById(String id){
        System.out.println("SERVICE Getting items");
        return itemDao.findItemsByUserId(id);
    }
}
