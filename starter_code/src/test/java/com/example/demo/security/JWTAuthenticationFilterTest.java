package com.example.demo.security;

import com.example.demo.model.persistence.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JWTAuthenticationFilterTest {

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    ObjectMapper objectMapper;

    @Captor
    ArgumentCaptor<String> tokenCaptor;

    JWTAuthenticationFilter jwtAuthenticationFilter;

    @Before
    public void setUp() {
        jwtAuthenticationFilter = new JWTAuthenticationFilter(authenticationManager, objectMapper);
    }

    @Test
    public final void testAttemptAuthentication() throws IOException {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse res = Mockito.mock(HttpServletResponse.class);
        ServletInputStream servletInputStream = Mockito.mock(ServletInputStream.class);
        Authentication auth = Mockito.mock(Authentication.class);

        User user = new User();
        user.setUsername("test");
        user.setPassword("password");

        Mockito.when(req.getInputStream()).thenReturn(servletInputStream);
        Mockito.when(objectMapper.readValue(Mockito.any(ServletInputStream.class), Mockito.eq(User.class))).thenReturn(user);
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);

        jwtAuthenticationFilter.attemptAuthentication(req, res);

        Mockito.verify(objectMapper, Mockito.times(1)).readValue(Mockito.any(ServletInputStream.class), Mockito.eq(User.class));
    }

    @Test(expected = RuntimeException.class)
    public final void testAttemptAuthenticationFail() throws IOException {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse res = Mockito.mock(HttpServletResponse.class);

        Mockito.when(req.getInputStream()).thenThrow(new IOException());

        jwtAuthenticationFilter.attemptAuthentication(req, res);
    }

    @Test
    public final void testSuccessfulAuthenticationReturnHeaderWithJWT() throws IOException, ServletException {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse res = Mockito.mock(HttpServletResponse.class);
        FilterChain chain = Mockito.mock(FilterChain.class);
        Authentication auth = Mockito.mock(Authentication.class);

        org.springframework.security.core.userdetails.User user
                = new org.springframework.security.core.userdetails.User(
                        "test", "password", new ArrayList<>());

        Mockito.when(auth.getPrincipal()).thenReturn(user);

        jwtAuthenticationFilter.successfulAuthentication(req, res, chain, auth);

        Mockito.verify(res, Mockito.times(1)).addHeader(Mockito.anyString(), tokenCaptor.capture());
        assertNotNull(tokenCaptor.getValue());
        assertTrue(tokenCaptor.getValue().contains("Bearer"));
    }
}
