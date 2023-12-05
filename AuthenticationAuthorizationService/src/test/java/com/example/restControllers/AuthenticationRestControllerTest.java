package com.example.restControllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.dto.AuthenticationRequestDTO;
import com.example.models.Role;
import com.example.models.User;
import com.example.security.JwtTokenProvider;
import com.example.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

public class AuthenticationRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthenticationRestController authenticationRestController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationRestController).build();
    }

    @Test
    public void testAuthenticateSuccess() throws Exception {
        // Подготовка данных и моков
        String login = "testUser";
        String password = "testPass";
        String token = "testToken";
        AuthenticationRequestDTO requestDTO = new AuthenticationRequestDTO();
        requestDTO.setLogin(login);
        requestDTO.setPassword(password);
        User mockUser = new User();
        mockUser.setRole(Role.USER);

        when(userService.findByLogin(login)).thenReturn(Optional.of(mockUser));
        when(jwtTokenProvider.createToken(login, mockUser.getRole().name())).thenReturn(token);

        // Выполнение и проверка
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value(login))
                .andExpect(jsonPath("$.token").value(token));
    }

    @Test
    public void testAuthenticateInvalidCredentials() throws Exception {
        String login = "invalidUser";
        String password = "wrongPass";
        AuthenticationRequestDTO requestDTO = new AuthenticationRequestDTO();
        requestDTO.setLogin(login);
        requestDTO.setPassword(password);

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testAuthenticateUserNotFound() throws Exception {
        String login = "nonExistingUser";
        String password = "pass";
        AuthenticationRequestDTO requestDTO = new AuthenticationRequestDTO();
        requestDTO.setLogin(login);
        requestDTO.setPassword(password);

        when(userService.findByLogin(login)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpect(status().isForbidden());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testLogout() throws Exception {
        mockMvc.perform(get("/api/v1/auth/logout"))
                .andExpect(cookie().exists("authToken"))
                .andExpect(cookie().maxAge("authToken", 0))
                .andExpect(cookie().path("authToken", "/"));
    }

    @Test
    public void testLogoutResponseStatus() throws Exception {
        mockMvc.perform(get("/api/v1/auth/logout"))
                .andExpect(status().isOk());
    }
}

