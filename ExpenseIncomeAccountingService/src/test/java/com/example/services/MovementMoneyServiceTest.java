package com.example.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.dto.MovementMoneyDTO;
import com.example.exception.MovementMoneyNotFoundException;
import com.example.exception.UserNotFoundException;
import com.example.models.MovementMoney;
import com.example.models.User;
import com.example.repositories.MovementMoneyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MovementMoneyServiceTest {

    @Mock
    private MovementMoneyRepository movementMoneyRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private MovementMoneyService movementMoneyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddMovementMoneySuccess() {
        MovementMoneyDTO movementMoneyDTO = new MovementMoneyDTO();
        MovementMoney movementMoney = new MovementMoney();
        User user = new User();

        when(userService.getUserByLogin(movementMoneyDTO.getLogin())).thenReturn(user);
        when(movementMoneyRepository.save(any(MovementMoney.class))).thenReturn(movementMoney);

        MovementMoney result = movementMoneyService.addMovementMoney(movementMoneyDTO);

        assertNotNull(result);
        assertEquals(movementMoney, result);
        verify(movementMoneyRepository).save(any(MovementMoney.class));
    }

    @Test
    public void testAddMovementMoneyUserNotFound() {
        MovementMoneyDTO movementMoneyDTO = new MovementMoneyDTO();
        movementMoneyDTO.setLogin("nonexistentUser");

        when(userService.getUserByLogin("nonexistentUser"))
                .thenThrow(new UserNotFoundException("User with login \"nonexistentUser\" not found"));

        assertThrows(UserNotFoundException.class, () -> movementMoneyService.addMovementMoney(movementMoneyDTO));
    }

    @Test
    public void testAddMovementMoneySaveException() {
        MovementMoneyDTO movementMoneyDTO = new MovementMoneyDTO();
        User user = new User();

        when(userService.getUserByLogin(movementMoneyDTO.getLogin())).thenReturn(user);
        when(movementMoneyRepository.save(any(MovementMoney.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> movementMoneyService.addMovementMoney(movementMoneyDTO));
    }

    @Test
    public void testAddMovementMoneyDataProcessing() {
        MovementMoneyDTO movementMoneyDTO = new MovementMoneyDTO();
        User user = new User();

        when(userService.getUserByLogin(movementMoneyDTO.getLogin())).thenReturn(user);
        when(movementMoneyRepository.save(any(MovementMoney.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MovementMoney result = movementMoneyService.addMovementMoney(movementMoneyDTO);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(movementMoneyDTO.getDescription(), result.getDescription());
        assertEquals(movementMoneyDTO.getAmount(), result.getAmount());
        assertEquals(movementMoneyDTO.getDate(), result.getDate());
        assertEquals(movementMoneyDTO.getType(), result.getType());
    }

    @Test
    public void testUpdateMovementMoneySuccess() {
        Long id = 1L;
        MovementMoneyDTO movementMoneyDTO = new MovementMoneyDTO();
        MovementMoney existingMovementMoney = new MovementMoney();
        User user = new User();

        when(movementMoneyRepository.findById(id)).thenReturn(Optional.of(existingMovementMoney));
        when(userService.getUserByLogin(movementMoneyDTO.getLogin())).thenReturn(user);
        when(movementMoneyRepository.save(any(MovementMoney.class))).thenReturn(existingMovementMoney);

        MovementMoney updatedMovementMoney = movementMoneyService.updateMovementMoney(id, movementMoneyDTO);

        assertNotNull(updatedMovementMoney);
        verify(movementMoneyRepository).save(existingMovementMoney);
        // Дополнительные проверки для обновленных полей
    }

    @Test
    public void testUpdateMovementMoneyNotFound() {
        Long id = 1L;
        MovementMoneyDTO movementMoneyDTO = new MovementMoneyDTO();

        when(movementMoneyRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(MovementMoneyNotFoundException.class,
                () -> movementMoneyService.updateMovementMoney(id, movementMoneyDTO));
    }

    @Test
    public void testUpdateMovementMoneyUserNotFound() {
        Long id = 1L;
        MovementMoneyDTO movementMoneyDTO = new MovementMoneyDTO();
        MovementMoney existingMovementMoney = new MovementMoney();

        when(movementMoneyRepository.findById(id)).thenReturn(Optional.of(existingMovementMoney));
        when(userService.getUserByLogin(movementMoneyDTO.getLogin()))
                .thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () -> movementMoneyService.updateMovementMoney(id, movementMoneyDTO));
    }

    @Test
    public void testDeleteMovementMoneySuccess() {
        Long id = 1L;

        doNothing().when(movementMoneyRepository).deleteById(id);

        movementMoneyService.deleteMovementMoney(id);

        verify(movementMoneyRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteMovementMoneyException() {
        Long id = 1L;

        doThrow(new RuntimeException("Database error")).when(movementMoneyRepository).deleteById(id);

        assertThrows(RuntimeException.class, () -> movementMoneyService.deleteMovementMoney(id));

        verify(movementMoneyRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteMovementMoneyWithInvalidId() {

        movementMoneyService.deleteMovementMoney(null);

        verify(movementMoneyRepository, never()).deleteById(any());
    }

    @Test
    public void testGetAllMovementMoneyByUserLoginSuccess() {
        String login = "user123";
        Long userId = 1L;
        List<MovementMoney> expectedMovements = List.of(new MovementMoney());

        when(userService.getId(login)).thenReturn(userId);
        when(movementMoneyRepository.findByUserId(userId)).thenReturn(expectedMovements);

        List<MovementMoney> result = movementMoneyService.getAllMovementMoneyByUserLogin(login);

        assertNotNull(result);
        assertEquals(expectedMovements, result);
    }

    @Test
    public void testGetAllMovementMoneyByUserLoginNoMovements() {
        String login = "user123";
        Long userId = 1L;

        when(userService.getId(login)).thenReturn(userId);
        when(movementMoneyRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        List<MovementMoney> result = movementMoneyService.getAllMovementMoneyByUserLogin(login);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAllMovementMoneyByUserLoginUserNotFound() {
        String login = "nonexistentUser";

        when(userService.getId(login)).thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () -> movementMoneyService.getAllMovementMoneyByUserLogin(login));
    }

}

