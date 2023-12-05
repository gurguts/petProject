package com.example.security;

import com.example.models.Status;
import com.example.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * SecurityUser is a record that implements UserDetails interface for Spring Security authentication.
 * <p>
 * This record is used to represent a user in the context of Spring Security, providing necessary
 * information required by the framework for authentication and authorization processes.
 * Parameters:
 * - username: The username of the user. Used for identifying the user during the authentication process.
 * - password: The password of the user. Used for validating credentials during authentication.
 * - authorityList: A list of SimpleGrantedAuthority objects representing the authorities granted to the user.
 * - isActive: A boolean indicating whether the user's account is active. An inactive account is not allowed
 * to authenticate.
 * <p>
 * The record overrides methods from the UserDetails interface to provide its own implementation for user details.
 * These methods include getting authorities, password, username, and account status flags (non-expired,
 * non-locked, credentials non-expired, and enabled).
 */
public record SecurityUser(
        String username,
        String password,
        List<SimpleGrantedAuthority> authorityList,
        boolean isActive
) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorityList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    /**
     * This static method creates a UserDetails object from a given User entity. UserDetails is a core
     * interface in Spring Security used for retrieving user-related data necessary for authentication
     * and authorization processes. The method maps the properties of the User entity to the appropriate
     * fields expected by Spring Security.
     * <p>
     * The UserDetails object includes:
     * - username: Derived from the user's login.
     * - password: The user's password.
     * - isEnabled: A boolean indicating if the account is active, based on the user's status.
     * - isAccountNonExpired: A boolean indicating if the account is non-expired, also based on the user's status.
     * - isAccountNonLocked: A boolean indicating if the account is non-locked, again based on the user's status.
     * - isCredentialsNonExpired: A boolean indicating if the credentials are non-expired, derived from the user's status.
     * - authorities: The set of authorities granted to the user, derived from the user's role.
     * <p>
     * This version of the application only uses the status whether the account is active for all boolean parameters
     *
     * @param user The User entity to be converted into UserDetails.
     * @return A UserDetails object representing the provided User entity.
     */
    public static UserDetails fromUser(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(), user.getPassword(),
                user.getStatus().equals(Status.ACTIVE),
                user.getStatus().equals(Status.ACTIVE),
                user.getStatus().equals(Status.ACTIVE),
                user.getStatus().equals(Status.ACTIVE),
                user.getRole().getAuthorities()
        );
    }
}
