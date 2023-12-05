package com.example.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.dto.RegistrationRequestDTO;
import com.example.exception.InvalidLoginException;
import com.example.exception.InvalidPasswordException;
import com.example.models.User;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByLoginExistingUser() {
        String login = "existingUser";
        User expectedUser = new User();
        expectedUser.setLogin(login);

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(expectedUser));

        Optional<User> result = userService.findByLogin(login);

        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());
    }

    @Test
    public void testFindByLoginNonExistingUser() {
        String login = "nonExistingUser";

        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

        Optional<User> result = userService.findByLogin(login);

        assertFalse(result.isPresent());
    }

    @Test
    public void testRegisterUserSuccess() {
        RegistrationRequestDTO requestDTO = new RegistrationRequestDTO();
        requestDTO.setLogin("newUser");
        requestDTO.setPassword("password1password*");
        requestDTO.setRole("USER");
        requestDTO.setStatus("ACTIVE");
        when(userRepository.existsByLogin(requestDTO.getLogin())).thenReturn(false);
        when(passwordEncoder.encode(requestDTO.getPassword())).thenReturn("encodedPassword");

        String result = userService.registerUser(requestDTO);

        assertEquals("User registered successfully", result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testRegisterUserExistingLogin() {
        RegistrationRequestDTO requestDTO = new RegistrationRequestDTO();
        requestDTO.setLogin("newUser");
        requestDTO.setPassword("password1password*");
        requestDTO.setRole("USER");
        requestDTO.setStatus("ACTIVE");
        when(userRepository.existsByLogin(requestDTO.getLogin())).thenReturn(true);

        assertThrows(InvalidLoginException.class, () -> userService.registerUser(requestDTO));
    }

    @Test
    public void testRegisterUserInvalidData() {
        RegistrationRequestDTO requestDTO = new RegistrationRequestDTO();
        requestDTO.setLogin("newUser");
        requestDTO.setPassword("newUser");
        requestDTO.setRole("USER");
        requestDTO.setStatus("ACTIVE");
        when(userRepository.existsByLogin(requestDTO.getLogin())).thenReturn(false);

        assertThrows(InvalidPasswordException.class, () -> userService.registerUser(requestDTO));
    }
}
