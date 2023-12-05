package com.example.models;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Enumeration of user roles within the application.
 * <p>
 * This enum defines different roles available in the application, such as USER and ADMIN.
 * <p>
 * The enum also includes methods to facilitate the conversion of these roles and permissions
 * into authorities that can be used with Spring Security's authorization mechanisms.
 */
public enum Role {
    USER(Set.of(Permission.DEVELOPERS_READ)),
    ADMIN(Set.of(Permission.DEVELOPERS_READ, Permission.DEVELOPERS_WRITE));
    private final Set<Permission> permissionSet;

    Role(Set<Permission> permissionSet) {
        this.permissionSet = permissionSet;
    }

    /**
     * This method returns the permissions that are linked to a specific role.
     *
     * @return A Set of Permission enums representing the specific permissions assigned to the role.
     */
    public Set<Permission> getPermissionSet() {
        return permissionSet;
    }

    /**
     * Converts the role's permissions into a set of Spring Security granted authorities.
     * <p>
     * It takes the set of permissions associated with a role, as defined in the Role enum,
     * and transforms each permission into a SimpleGrantedAuthority object. These granted
     * authority objects are then used by Spring Security to make authorization decisions.
     *
     * @return A Set of SimpleGrantedAuthority objects, each representing a permission associated with the role.
     */
    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissionSet().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
