package com.example.demo.security;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDetailsServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public final void testLoadUserByUsername() {
        User user = Mockito.mock(User.class);
        Mockito.when(user.getUsername()).thenReturn("test");
        Mockito.when(user.getPassword()).thenReturn("password");
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user);

        assertNotNull(userDetailsService.loadUserByUsername("test"));
    }

    @Test(expected = UsernameNotFoundException.class)
    public final void testLoadUserByUsernameNoUser() {
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(null);

        userDetailsService.loadUserByUsername("test");
    }
}
