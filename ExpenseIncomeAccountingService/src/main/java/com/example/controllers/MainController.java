package com.example.controllers;

import com.example.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This controller is responsible for handling HTTP requests related to the main areas of the application.
 * It is mapped to the "/api/v1/main" path and provides endpoints for accessing the main and list pages of
 * the application.
 */
@Controller
@RequestMapping("/api/v1/main")
public class MainController {
    /**
     * UserService to extract login from token
     */
    private final UserService userService;

    public MainController(UserService userService) {
        this.userService = userService;
    }

    /**
     * This method handles GET requests to the base URL of the MainController. It is responsible for
     * rendering the main page view.
     * <p>
     * The process involves:
     * - Extracting the user's login from the JWT token using the UserService.
     * - Adding the extracted login to the model, which will be used in the view layer for display or further logic.
     * - Returning the view name "main", which corresponds to the main page template.
     *
     * @param httpServletRequest The incoming HTTP request containing the JWT token.
     * @param model              The model object used to pass data to the view template.
     * @return The name of the view template to be rendered ("main").
     */
    @GetMapping
    public String getMainPage(HttpServletRequest httpServletRequest, Model model) {
        String login = userService.processUserFromJwt(httpServletRequest);
        model.addAttribute("login", login);
        return "main";
    }

    /**
     * This method handles GET requests for the "/list" endpoint of the MainController. It is responsible
     * for displaying the list page, where specific list-based content of the application is presented.
     *
     * @return The name of the view template to be rendered ("list").
     */
    @GetMapping("/list")
    public String getListPage() {
        return "list";
    }
}