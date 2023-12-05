package com.example.restControllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.dto.RegistrationRequestDTO;
import com.example.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class RegistrationRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private RegistrationRestController registrationRestController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(registrationRestController).build();
    }

    @Test
    public void testRegisterUserSuccess() throws Exception {
        RegistrationRequestDTO requestDTO = new RegistrationRequestDTO();
        requestDTO.setLogin("user");
        requestDTO.setPassword("password");
        when(userService.registerUser(requestDTO)).thenReturn("User registered successfully");

        mockMvc.perform(post("/api/v1/reg/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    public void testRegisterUserFailure() throws Exception {
        RegistrationRequestDTO requestDTO = new RegistrationRequestDTO();
        requestDTO.setLogin("user");
        requestDTO.setPassword("password");
        when(userService.registerUser(requestDTO)).thenThrow(new IllegalArgumentException("Invalid data"));

        mockMvc.perform(post("/api/v1/reg/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid data"));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

