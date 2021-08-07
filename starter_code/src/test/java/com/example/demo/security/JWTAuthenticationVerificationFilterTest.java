package com.example.demo.security;

import com.auth0.jwt.JWT;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JWTAuthenticationVerificationFilterTest {

    @MockBean
    AuthenticationManager authenticationManager;

    JWTAuthenticationVerificationFilter jwtAuthenticationVerificationFilter;

    @Before
    public void setUp() {
        jwtAuthenticationVerificationFilter = new JWTAuthenticationVerificationFilter(authenticationManager);
    }

    @Test
    public final void testDoFilterInternalNoUser() throws IOException, ServletException {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse res = Mockito.mock(HttpServletResponse.class);
        FilterChain chain = Mockito.mock(FilterChain.class);

        String token = SecurityConstants.TOKEN_PREFIX + JWT.create()
                .withSubject("testuser")
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(HMAC512(SecurityConstants.SECRET.getBytes()));

        Mockito.when(req.getHeader(Mockito.anyString()))
                .thenReturn(token);

        jwtAuthenticationVerificationFilter.doFilterInternal(req, res, chain);

        Mockito.verify(req, Mockito.times(2)).getHeader(Mockito.anyString());
    }

    @Test
    public final void testDoFilterInternalBadHeader() throws IOException, ServletException {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse res = Mockito.mock(HttpServletResponse.class);
        FilterChain chain = Mockito.mock(FilterChain.class);

        Mockito.when(req.getHeader(Mockito.anyString())).thenReturn(null);

        jwtAuthenticationVerificationFilter.doFilterInternal(req, res, chain);

        Mockito.when(req.getHeader(Mockito.anyString())).thenReturn("");

        jwtAuthenticationVerificationFilter.doFilterInternal(req, res, chain);

        Mockito.verify(chain, Mockito.times(2)).doFilter(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class));
    }
}
