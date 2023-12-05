package com.example.restControllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

import com.example.dto.MovementMoneyDTO;
import com.example.services.BalanceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BalanceRestController.class)
public class BalanceRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BalanceService balanceService;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetBalance() throws Exception {

        MovementMoneyDTO dto1 = new MovementMoneyDTO();
        List<MovementMoneyDTO> moneyDTOList = Arrays.asList(dto1, dto1);
        double expectedBalance = 100.0;

        when(balanceService.calculateBalance(anyList())).thenReturn(expectedBalance);

        mockMvc.perform(post("/api/v1/balance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(moneyDTOList)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(String.valueOf(expectedBalance))));
    }

    @Test
    public void testGetBalanceWithEmptyList() throws Exception {

        List<MovementMoneyDTO> emptyList = Collections.emptyList();

        when(balanceService.calculateBalance(emptyList)).thenReturn(0.0);

        mockMvc.perform(post("/api/v1/balance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(emptyList)))
                .andExpect(status().isOk())
                .andExpect(content().string("0.0"));
    }

    @Test
    public void testGetBalanceWithInvalidData() throws Exception {

        String invalidContent = "invalid data";

        mockMvc.perform(post("/api/v1/balance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidContent))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetBalanceServiceException() throws Exception {

        MovementMoneyDTO dto = new MovementMoneyDTO();
        List<MovementMoneyDTO> moneyDTOList = List.of(dto);

        when(balanceService.calculateBalance(anyList())).thenThrow(new RuntimeException());

        mockMvc.perform(post("/api/v1/balance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(moneyDTOList)))
                .andExpect(status().isInternalServerError());
    }

}
