package com.example.services;

import com.example.exception.UserNotFoundException;
import com.example.models.User;
import com.example.repositories.UserRepository;
import com.example.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This service provides functionalities related to user management, such as processing user data from JWT tokens,
 * retrieving user IDs, and fetching user entities based on login credentials.
 */
@Service
public class UserService {

    /**
     * UserRepository is used for database operations
     */
    private final UserRepository userRepository;

    /**
     * jwtTokenProvider is used for token related processes
     */
    private final JwtTokenProvider jwtTokenProvider;


    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * This method extracts the user's login information from a JWT token found in the provided HttpServletRequest.
     * Additionally, it ensures that a user account exists for the extracted login; if not, a new user account
     * is created with the given login.
     * <p>
     * Steps:
     * - Extracts the JWT token from the request.
     * - Retrieves the user login from the token.
     * - Checks if a user with this login exists in the database and creates a new user if not.
     *
     * @param request The HttpServletRequest containing the JWT token.
     * @return The login of the user extracted from the JWT token.
     */
    public String processUserFromJwt(HttpServletRequest request) {
        String jwtToken = jwtTokenProvider.resolveToken(request);
        String userLogin = jwtTokenProvider.getLoginFromToken(jwtToken);
        checkAndCreateUser(userLogin);
        return userLogin;
    }

    /**
     * This private method is used to verify whether a user with the specified login already exists in the
     * database. If the user does not exist, it creates a new User entity with the given login and persists it
     * to the database.
     * <p>
     * Process:
     * - Checks the existence of a user with the provided login using the UserRepository.
     * - If the user does not exist, creates a new User entity, sets its login, and saves it to the database.
     *
     * @param userLogin The login identifier of the user to be checked and potentially created.
     */
    private void checkAndCreateUser(String userLogin) {
        if (!userRepository.existsByLogin(userLogin)) {
            User user = new User();
            user.setLogin(userLogin);
            userRepository.save(user);
        }
    }

    /**
     * This method fetches the unique identifier (ID) of a user from the database using their login.
     * It utilizes the UserRepository to find the user, returning an Optional<User> which is then
     * processed to extract the user's ID. If the user is not found, a UserNotFoundException is thrown.
     *
     * @param login The login identifier of the user whose ID is being requested.
     * @return The ID of the user associated with the given login.
     * @throws UserNotFoundException if no user is found with the provided login.
     */
    public Long getId(String login) {
        Optional<User> user = userRepository.findByLogin(login);
        return user
                .orElseThrow(() -> new UserNotFoundException("User with login \"" + login + "\" not found"))
                .getId();
    }

    /**
     * This method searches for a user in the database using the provided login. It leverages the UserRepository
     * to perform the search, which returns an Optional<User>. If a user with the specified login is found, the
     * method returns the User entity. Otherwise, it throws a UserNotFoundException.
     *
     * @param login The login identifier used to locate the user.
     * @return The User entity associated with the given login.
     * @throws UserNotFoundException if no user is found with the provided login.
     */
    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("User with login \"" + login + "\" not found"));
    }
}
