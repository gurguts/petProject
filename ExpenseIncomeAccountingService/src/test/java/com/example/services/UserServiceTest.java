package com.example.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.exception.UserNotFoundException;
import com.example.models.User;
import com.example.repositories.UserRepository;
import com.example.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessUserFromJwtExistingUser() {
        String jwtToken = "jwtToken";
        String userLogin = "existingUser";

        when(jwtTokenProvider.resolveToken(request)).thenReturn(jwtToken);
        when(jwtTokenProvider.getLoginFromToken(jwtToken)).thenReturn(userLogin);
        when(userRepository.existsByLogin(userLogin)).thenReturn(true);

        String resultLogin = userService.processUserFromJwt(request);

        assertEquals(userLogin, resultLogin);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testProcessUserFromJwtNewUser() {
        String jwtToken = "jwtToken";
        String newUserLogin = "newUser";

        when(jwtTokenProvider.resolveToken(request)).thenReturn(jwtToken);
        when(jwtTokenProvider.getLoginFromToken(jwtToken)).thenReturn(newUserLogin);
        when(userRepository.existsByLogin(newUserLogin)).thenReturn(false);

        String resultLogin = userService.processUserFromJwt(request);

        assertEquals(newUserLogin, resultLogin);
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testProcessUserFromJwtTokenException() {
        when(jwtTokenProvider.resolveToken(request)).thenThrow(new RuntimeException("Token resolving error"));

        assertThrows(RuntimeException.class, () -> userService.processUserFromJwt(request));
    }

    @Test
    public void testGetIdSuccess() {
        String userLogin = "existingUser";
        Long expectedUserId = 1L;
        User user = new User();
        user.setId(expectedUserId);

        when(userRepository.findByLogin(userLogin)).thenReturn(Optional.of(user));

        Long userId = userService.getId(userLogin);

        assertEquals(expectedUserId, userId);
    }

    @Test
    public void testGetIdUserNotFound() {
        String nonExistentLogin = "nonExistentUser";

        when(userRepository.findByLogin(nonExistentLogin)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getId(nonExistentLogin));
    }

    @Test
    public void testGetUserByLoginSuccess() {
        String userLogin = "existingUser";
        User expectedUser = new User();
        expectedUser.setLogin(userLogin);

        when(userRepository.findByLogin(userLogin)).thenReturn(Optional.of(expectedUser));

        User resultUser = userService.getUserByLogin(userLogin);

        assertNotNull(resultUser);
        assertEquals(expectedUser.getLogin(), resultUser.getLogin());
    }

    @Test
    public void testGetUserByLoginUserNotFound() {
        String nonExistentLogin = "nonExistentUser";

        when(userRepository.findByLogin(nonExistentLogin)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByLogin(nonExistentLogin));
    }

}
