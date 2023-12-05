package com.example.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.exception.JwtAuthenticationException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;

public class JwtTokenProviderTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidateTokenValid() {
        String validToken = "validToken";
        when(jwtTokenProvider.validateToken(validToken)).thenReturn(true);

        boolean isValid = jwtTokenProvider.validateToken(validToken);

        assertTrue(isValid);
    }

    @Test
    public void testValidateTokenInvalid() {
        String invalidToken = "invalidToken";
        when(jwtTokenProvider.validateToken(invalidToken))
                .thenThrow(new JwtAuthenticationException("JWT token is expired or invalid", HttpStatus.UNAUTHORIZED));

        assertThrows(JwtAuthenticationException.class, () -> jwtTokenProvider.validateToken(invalidToken));
    }

    @Test
    public void testGetAuthentication() {
        String token = "validToken";
        String username = "user";
        List<SimpleGrantedAuthority> expectedAuthorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        when(jwtTokenProvider.getUsername(token)).thenReturn(username);
        when(jwtTokenProvider.getAuthentication(token))
                .thenReturn(new UsernamePasswordAuthenticationToken(username, "", expectedAuthorities));

        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        assertNotNull(authentication);
        assertEquals(username, authentication.getName());
        assertEquals(expectedAuthorities, authentication.getAuthorities());
        assertTrue(authentication instanceof UsernamePasswordAuthenticationToken);
    }

    @Test
    public void testGetAuthenticationWithMultipleRoles() {
        String token = "validToken";
        String username = "user";
        List<GrantedAuthority> expectedAuthorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );

        when(jwtTokenProvider.getUsername(token)).thenReturn(username);
        when(jwtTokenProvider.getAuthentication(token))
                .thenReturn(new UsernamePasswordAuthenticationToken(username, "", expectedAuthorities));

        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        assertNotNull(authentication);
        assertEquals(username, authentication.getName());
        assertEquals(expectedAuthorities, authentication.getAuthorities());
        assertTrue(authentication instanceof UsernamePasswordAuthenticationToken);
    }

    @Test
    public void testGetUsernameValidToken() {
        String validToken = "yourValidTokenHere";
        String expectedUsername = "expectedUsername";

        when(jwtTokenProvider.getUsername(validToken)).thenReturn(expectedUsername);

        String username = jwtTokenProvider.getUsername(validToken);

        assertEquals(expectedUsername, username);
    }

    @Test
    public void testResolveTokenWithTokenInCookies() {
        Cookie[] cookies = new Cookie[]{new Cookie("authToken", "tokenValue")};
        when(request.getCookies()).thenReturn(cookies);

        when(jwtTokenProvider.resolveToken(request)).thenReturn("tokenValue");

        String token = jwtTokenProvider.resolveToken(request);

        assertEquals("tokenValue", token);
    }

    @Test
    public void testResolveTokenWithNoAuthTokenCookie() {
        Cookie[] cookies = new Cookie[]{new Cookie("otherCookie", "value")};
        when(request.getCookies()).thenReturn(cookies);

        when(jwtTokenProvider.resolveToken(request)).thenReturn(null);

        String token = jwtTokenProvider.resolveToken(request);

        assertNull(token);
    }

    @Test
    public void testResolveTokenWithNoCookies() {
        when(request.getCookies()).thenReturn(null);

        when(jwtTokenProvider.resolveToken(request)).thenReturn(null);

        String token = jwtTokenProvider.resolveToken(request);

        assertNull(token);
    }

    @Test
    public void testGetLoginFromToken() {
        String validToken = "yourValidTokenHere";
        String expectedLogin = "expectedLogin";

        when(jwtTokenProvider.getLoginFromToken(validToken)).thenReturn(expectedLogin);

        String login = jwtTokenProvider.getLoginFromToken(validToken);

        assertEquals(expectedLogin, login);
    }
}
