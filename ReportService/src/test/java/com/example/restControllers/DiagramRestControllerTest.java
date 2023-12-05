package com.example.restControllers;

import com.example.dto.MovementMoneyDTO;
import com.example.models.DiagramData;
import com.example.services.DiagramService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DiagramRestController.class)
public class DiagramRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DiagramService diagramService;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetReportDiagram() throws Exception {

        List<DiagramData> diagramDataList = new ArrayList<>();
        List<DiagramData> expectedDataList = new ArrayList<>();

        when(diagramService.getDataDiagram(anyList())).thenReturn(expectedDataList);

        mockMvc.perform(post("/api/v1/diagram")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(diagramDataList)))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(expectedDataList)));
    }

    @Test
    public void testGetReportDiagramWithEmptyList() throws Exception {

        List<MovementMoneyDTO> emptyList = Collections.emptyList();

        when(diagramService.getDataDiagram(emptyList)).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/api/v1/diagram")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(emptyList)))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(Collections.emptyList())));
    }

    @Test
    public void testGetReportDiagramWithInvalidData() throws Exception {

        String invalidContent = "invalid data";

        mockMvc.perform(post("/api/v1/diagram")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidContent))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetReportDiagramServiceException() throws Exception {

        MovementMoneyDTO dto = new MovementMoneyDTO();
        List<MovementMoneyDTO> moneyDTOList = List.of(dto);

        when(diagramService.getDataDiagram(anyList())).thenThrow(new RuntimeException());

        mockMvc.perform(post("/api/v1/diagram")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(moneyDTOList)))
                .andExpect(status().isInternalServerError());
    }
}
