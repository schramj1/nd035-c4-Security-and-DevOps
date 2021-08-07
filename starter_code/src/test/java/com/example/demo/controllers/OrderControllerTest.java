package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderControllerTest {

    @Autowired
    OrderController orderController;

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    UserRepository userRepository;

    @Test
    public final void testSubmitWithUser() {
        User user = new User();

        Cart cart = new Cart();
        cart.setTotal(new BigDecimal(10.0));
        cart.setItems(new ArrayList<>());

        user.setCart(cart);

        Mockito.when(userRepository.findByUsername(Mockito.eq("test"))).thenReturn(user);

        ResponseEntity<UserOrder> responseEntity = orderController.submit("test");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Mockito.verify(orderRepository, Mockito.times(1)).save(Mockito.any(UserOrder.class));
    }

    @Test
    public final void testSubmitNullUser() {
        Mockito.when(userRepository.findByUsername(Mockito.eq("test"))).thenReturn(null);

        ResponseEntity<UserOrder> responseEntity = orderController.submit("test");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public final void testGetOrdersForUser() {
        Mockito.when(userRepository.findByUsername(Mockito.eq("test"))).thenReturn(new User());
        Mockito.when(orderRepository.findByUser(Mockito.any(User.class))).thenReturn(new ArrayList<>());

        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("test");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public final void testGetOrdersForNullUser() {
        Mockito.when(userRepository.findByUsername(Mockito.eq("test"))).thenReturn(null);

        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("test");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}
