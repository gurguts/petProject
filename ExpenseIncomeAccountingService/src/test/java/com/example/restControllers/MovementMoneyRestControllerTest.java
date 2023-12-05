package com.example.restControllers;

import com.example.dto.MovementMoneyDTO;
import com.example.models.MovementMoney;
import com.example.services.MovementMoneyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MovementMoneyRestControllerTest {

    @Mock
    private MovementMoneyService movementMoneyService;

    @InjectMocks
    private MovementMoneyRestController movementMoneyRestController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddExpenseSuccess() {
        MovementMoneyDTO movementMoneyDTO = new MovementMoneyDTO();
        MovementMoney expectedMovementMoney = new MovementMoney();

        when(movementMoneyService.addMovementMoney(movementMoneyDTO)).thenReturn(expectedMovementMoney);

        ResponseEntity<MovementMoney> response = movementMoneyRestController.addExpense(movementMoneyDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedMovementMoney, response.getBody());
    }

    @Test
    public void testAddExpenseException() {
        MovementMoneyDTO movementMoneyDTO = new MovementMoneyDTO();

        when(movementMoneyService.addMovementMoney(movementMoneyDTO))
                .thenThrow(new RuntimeException("Service exception"));

        assertThrows(RuntimeException.class, () -> movementMoneyRestController.addExpense(movementMoneyDTO));
    }

    @Test
    public void testUpdateExpenseSuccess() {
        Long id = 1L;
        MovementMoneyDTO movementMoneyDTO = new MovementMoneyDTO();
        MovementMoney updatedMovementMoney = new MovementMoney();

        when(movementMoneyService.updateMovementMoney(id, movementMoneyDTO)).thenReturn(updatedMovementMoney);

        ResponseEntity<MovementMoney> response = movementMoneyRestController.updateExpense(id, movementMoneyDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedMovementMoney, response.getBody());
    }

    @Test
    public void testUpdateExpenseException() {
        Long id = 1L;
        MovementMoneyDTO movementMoneyDTO = new MovementMoneyDTO();

        when(movementMoneyService.updateMovementMoney(id, movementMoneyDTO))
                .thenThrow(new RuntimeException("Service exception"));

        assertThrows(RuntimeException.class, () -> movementMoneyRestController.updateExpense(id, movementMoneyDTO));
    }

    @Test
    public void testDeleteExpenseSuccess() {
        Long id = 1L; // Пример идентификатора

        doNothing().when(movementMoneyService).deleteMovementMoney(id);

        movementMoneyRestController.deleteExpense(id);

        verify(movementMoneyService, times(1)).deleteMovementMoney(id);
    }

    @Test
    public void testDeleteExpenseException() {
        Long id = 1L; // Пример идентификатора

        doThrow(new RuntimeException("Service exception")).when(movementMoneyService).deleteMovementMoney(id);

        assertThrows(RuntimeException.class, () -> movementMoneyRestController.deleteExpense(id));

        verify(movementMoneyService, times(1)).deleteMovementMoney(id);
    }

    @Test
    public void testGetAllMoveMoneySuccess() {
        String login = "user123";
        List<MovementMoney> movementMoneyList = List.of(new MovementMoney());

        when(movementMoneyService.getAllMovementMoneyByUserLogin(login)).thenReturn(movementMoneyList);

        ResponseEntity<List<MovementMoney>> response = movementMoneyRestController.getAllMoveMoney(login);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(movementMoneyList, response.getBody());
    }

    @Test
    public void testGetAllMoveMoneyNoMovements() {
        String login = "user123";
        when(movementMoneyService.getAllMovementMoneyByUserLogin(login)).thenReturn(Collections.emptyList());

        ResponseEntity<List<MovementMoney>> response = movementMoneyRestController.getAllMoveMoney(login);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isEmpty());
    }

}