package com.example.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class WebSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldPermitAllForSpecificUrls() throws Exception {
        mockMvc.perform(get("/favicon.ico"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/css/background.css"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/js/main/addButtons.js"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldDenyAccessForUnauthenticatedUsers() throws Exception {
        mockMvc.perform(get("/api/v1/main/list"))
                .andExpect(status().isForbidden());
    }
}

