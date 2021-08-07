package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartControllerTest {

    @Autowired
    private CartController cartController;

    @Autowired
    private CartRepository cartRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ItemRepository itemRepository;

    private Item item;

    private User user;

    @Before
    public void setUp() {
        item = new Item();
        item.setId(1L);
        item.setPrice(new BigDecimal(10.0));
        item.setName("Gizmo");
        item.setDescription("Is a goober");

        user = new User();
        user.setCart(new Cart());
    }

    @After
    public void tearDown() {
        cartRepository.deleteAll();
    }

    @Test
    public final void testAddToCart() {
        Mockito.when(itemRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findByUsername(Mockito.eq("test"))).thenReturn(user);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);

        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, cartRepository.findAll().size());
    }

    @Test
    public final void testAddToCartNullUser() {
        Mockito.when(itemRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findByUsername(Mockito.eq("test"))).thenReturn(null);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);

        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public final void testAddInvalidItemToCart() {
        Mockito.when(itemRepository.findById(Mockito.eq(1L))).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByUsername(Mockito.eq("test"))).thenReturn(user);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);

        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public final void testRemoveFromCart() {
        Mockito.when(itemRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findByUsername(Mockito.eq("test"))).thenReturn(user);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);

        user.getCart().addItem(item);

        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, cartRepository.findAll().size());
    }

    @Test
    public final void testRemoveFromCartNullUser() {
        Mockito.when(itemRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findByUsername(Mockito.eq("test"))).thenReturn(null);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);

        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public final void testRemoveInvalidItemFromCart() {
        Mockito.when(itemRepository.findById(Mockito.eq(1L))).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByUsername(Mockito.eq("test"))).thenReturn(user);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);

        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}
