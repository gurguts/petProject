package com.example.security;

import com.example.models.User;
import com.example.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service implementation of UserDetailsService for loading user details.
 * <p>
 * This class provides an implementation of the UserDetailsService interface required by
 * Spring Security for user authentication. The service is used to retrieve user details
 * from the database based on the login.
 * <p>
 * The service is annotated with @Service and a specific name "userDetailsServiceImpl" to
 * distinguish it in the Spring context, especially if there are multiple UserDetailsService
 * implementations.
 */
@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * The repository used for retrieving user data from the database.
     */
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * This method is an implementation of the loadUserByUsername method from the UserDetailsService interface,
     * which is a core part of Spring Security's authentication process.
     * <p>
     * The method performs the following operations:
     * - Uses the userRepository to search for a user by the given login.
     * - If a user is found, it converts the User entity to a UserDetails object using the SecurityUser.fromUser method.
     * - If no user is found with the provided login, it throws a UsernameNotFoundException with an appropriate message.
     *
     * @param login The login identifier of the user whose details need to be loaded.
     * @return A UserDetails object containing the user's information.
     * @throws UsernameNotFoundException If no user is found with the specified login.
     */
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login).orElseThrow(() ->
                new UsernameNotFoundException("User doesn't exist"));
        return SecurityUser.fromUser(user);
    }
}
