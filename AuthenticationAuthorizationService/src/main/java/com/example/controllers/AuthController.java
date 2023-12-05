package com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The controller handles web requests related to user authentication, namely displaying the login page.
 * <p>
 * It uses the @Controller annotation to indicate that it is a web controller capable of handling HTTP requests.
 * The @RequestMapping annotation without specific path mappings specifies that this controller's methods will
 * handle requests for the root path and its sub-paths.
 */
@Controller
@RequestMapping
public class AuthController {

    /**
     * Handles the request to get the login page.
     * <p>
     * When a GET request is made to the root URL ("/"), this method returns the name
     * of the view (in this case, "login") to be rendered. The @GetMapping annotation specifies that
     * this method should respond to GET requests.
     * <p>
     * The @CrossOrigin annotation allows cross-origin requests from the specified origin
     * (<a href="http://localhost:8081">...</a> in this case), which is useful in development environments
     * or scenarios where the front-end and back-end are served from different hosts.
     *
     * @return The name of the view to be rendered, in this case, "login".
     */
    @GetMapping("/")
    @CrossOrigin(origins = "http://localhost:8081")
    public String getLoginPage() {
        return "login";
    }
}
