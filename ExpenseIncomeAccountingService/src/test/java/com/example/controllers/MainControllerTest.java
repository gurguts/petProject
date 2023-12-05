package com.example.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

public class MainControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private Model model;

    private MainController mainController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mainController = new MainController(userService);
    }

    @Test
    void testGetMainPage() {
        // Arrange
        String expectedLogin = "userLogin";
        when(userService.processUserFromJwt(httpServletRequest)).thenReturn(expectedLogin);

        // Act
        String viewName = mainController.getMainPage(httpServletRequest, model);

        // Assert
        verify(userService).processUserFromJwt(httpServletRequest);
        verify(model).addAttribute("login", expectedLogin);
        assertEquals("main", viewName);
    }

    @Test
    void testGetListPage() {
        // Act
        String viewName = mainController.getListPage();

        // Assert
        assertEquals("list", viewName);
    }
}
