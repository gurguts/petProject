package com.example.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        AuthController authController = new AuthController();
        mockMvc = standaloneSetup(authController).build();
    }

    @Test
    public void testGetLoginPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(view().name("login"));
    }

    @Test
    public void testGetLoginPageStatus() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }
}

