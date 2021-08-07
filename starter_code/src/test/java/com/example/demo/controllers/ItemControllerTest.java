package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemControllerTest {

    @Autowired
    ItemController itemController;

    @Autowired
    ItemRepository itemRepository;

    @Before
    public final void setUp() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Gizmo");
        item.setDescription("Goober");
        item.setPrice(new BigDecimal(10.0));

        itemRepository.save(item);
    }

    @After
    public final void tearDown() {
        itemRepository.deleteAll();
    }

    @Test
    public final void testGetItems() {
        ResponseEntity<List<Item>> responseEntity = itemController.getItems();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().size());
    }

    @Test
    public final void testGetItemById() {
        ResponseEntity<Item> responseEntity = itemController.getItemById(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Goober", responseEntity.getBody().getDescription());
    }

    @Test
    public final void testGetItemsByName() {
        ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("Gizmo");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().size());
    }

    @Test
    public final void testGetItemsByNameNotFound() {
        ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("Gadget");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}
