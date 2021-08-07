package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserController userController;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @After
    public void tearDown() {
        cartRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public final void testCreateUserWithOkResponse() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("password");
        createUserRequest.setConfirmPassword("password");

        ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);

        assertEquals(1, userRepository.findAll().size());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public final void testCreateUserWithExistingUser() {
        User user = new User();
        user.setUsername("test");
        user.setPassword(bCryptPasswordEncoder.encode("password"));
        userRepository.save(user);

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("password2");
        createUserRequest.setConfirmPassword("password2");

        ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);

        assertEquals(1, userRepository.findAll().size());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public final void testCreateUserWithWrongConfirmPassword() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("password");
        createUserRequest.setConfirmPassword("password2");

        ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);

        assertEquals(0, userRepository.findAll().size());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public final void testCreateUserWithShortPassword() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("pass");
        createUserRequest.setConfirmPassword("pass");

        ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);

        assertEquals(0, userRepository.findAll().size());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public final void testFindByUsername() {
        User user = new User();
        user.setUsername("test");
        user.setPassword(bCryptPasswordEncoder.encode("password"));
        userRepository.save(user);

        ResponseEntity<User> responseEntity = userController.findByUserName("test");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(user.getId(), responseEntity.getBody().getId());
    }

    @Test
    public final void testFindByUsernameFail() {
        ResponseEntity<User> responseEntity = userController.findByUserName("test");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public final void testFindById() {
        User user = new User();
        user.setUsername("test");
        user.setPassword(bCryptPasswordEncoder.encode("password"));
        userRepository.save(user);

        ResponseEntity<User> responseEntity = userController.findById(user.getId());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(user.getId(), responseEntity.getBody().getId());
    }

    @Test
    public final void testFindByIdFail() {
        ResponseEntity<User> responseEntity = userController.findById(1L);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}
