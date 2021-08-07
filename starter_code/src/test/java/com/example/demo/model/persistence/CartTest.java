package com.example.demo.model.persistence;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartTest {

    Item item;

    @Before
    public void setUp() {
        item = new Item();
        item.setPrice(new BigDecimal(10.0));
    }

    @Test
    public final void testAddItem() {
        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());
        cart.setTotal(new BigDecimal(0.0));

        cart.addItem(item);

        assertEquals(10.0, cart.getTotal().doubleValue(), 0.0);
        assertEquals(1, cart.getItems().size());
    }

    @Test
    public final void testAddItemNullListAndNullTotal() {
        Cart cart = new Cart();
        cart.addItem(item);

        assertEquals(10.0, cart.getTotal().doubleValue(), 0.0);
        assertEquals(1, cart.getItems().size());
    }

    @Test
    public final void testRemoveItem() {
        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());
        cart.setTotal(new BigDecimal(0.0));

        cart.removeItem(item);

        assertEquals(-10.0, cart.getTotal().doubleValue(), 0.0);
        assertEquals(0, cart.getItems().size());
    }

    @Test
    public final void testRemoveItemNullListAndNullTotal() {
        Cart cart = new Cart();

        cart.removeItem(item);

        assertEquals(-10.0, cart.getTotal().doubleValue(), 0.0);
        assertEquals(0, cart.getItems().size());
    }
}
