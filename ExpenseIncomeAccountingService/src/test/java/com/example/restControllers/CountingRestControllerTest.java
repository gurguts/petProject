package com.example.restControllers;

import com.example.dto.DiagramDataDTO;
import com.example.models.MovementMoney;
import com.example.services.MovementMoneyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CountingRestControllerTest {

    private CountingRestController controller;
    private MovementMoneyService movementMoneyService;
    private RestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        movementMoneyService = mock(MovementMoneyService.class);
        restTemplate = mock(RestTemplate.class);
        controller = new CountingRestController(movementMoneyService, restTemplate);
    }

    @Test
    public void testGetBalance() {
        String login = "user123";
        List<MovementMoney> moneyList = List.of(new MovementMoney());
        when(movementMoneyService.getAllMovementMoneyByUserLogin(login)).thenReturn(moneyList);

        double expectedBalance = 100.0;
        when(restTemplate.postForEntity(
                "http://localhost:8082/api/v1/balance",
                moneyList,
                Double.class))
                .thenReturn(ResponseEntity.ok(expectedBalance));

        ResponseEntity<Double> response = controller.getBalance(login);

        assertEquals(expectedBalance, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetBalanceWithNoMovements() {
        String login = "user123";
        when(movementMoneyService.getAllMovementMoneyByUserLogin(login)).thenReturn(Collections.emptyList());

        ResponseEntity<Double> response = controller.getBalance(login);

        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testGetBalanceServiceException() {
        String login = "user123";
        when(movementMoneyService.getAllMovementMoneyByUserLogin(login))
                .thenThrow(new RuntimeException("Service exception"));

        ResponseEntity<Double> response = controller.getBalance(login);

        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetDiagramDataSuccess() {
        String login = "user123";
        List<MovementMoney> moneyList = List.of(new MovementMoney());
        List<DiagramDataDTO> diagramData = List.of(new DiagramDataDTO());

        when(movementMoneyService.getAllMovementMoneyByUserLogin(login)).thenReturn(moneyList);
        when(restTemplate.exchange(
                "http://localhost:8082/api/v1/diagram",
                HttpMethod.POST,
                new HttpEntity<>(moneyList),
                new ParameterizedTypeReference<List<DiagramDataDTO>>() {
                }))
                .thenReturn(new ResponseEntity<>(diagramData, HttpStatus.OK));

        ResponseEntity<List<DiagramDataDTO>> response = controller.getDiagramData(login);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(diagramData, response.getBody());
    }

    @Test
    public void testGetDiagramDataWithNoMovements() {
        String login = "user123";
        when(movementMoneyService.getAllMovementMoneyByUserLogin(login)).thenReturn(Collections.emptyList());

        ResponseEntity<List<DiagramDataDTO>> response = controller.getDiagramData(login);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testGetDiagramDataServiceException() {
        String login = "user123";
        when(movementMoneyService.getAllMovementMoneyByUserLogin(login))
                .thenThrow(new RuntimeException("Service exception"));

        ResponseEntity<List<DiagramDataDTO>> response = controller.getDiagramData(login);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}