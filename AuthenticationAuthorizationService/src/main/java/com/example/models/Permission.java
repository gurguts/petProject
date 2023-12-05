package com.example.models;

/**
 * Not used in this version. But for future expansion of the application you may need
 * <p>
 * Enumeration of application-specific permissions.
 * <p>
 * This enum defines various permissions within the application, particularly focusing
 * on operations related to 'developers'. Each enum constant represents a specific
 * permission, encapsulating the action and the target entity in a single value. This
 * approach allows for a clear and manageable way of handling permissions throughout
 * the application.
 * <p>
 * This enum can be used in conjunction with security frameworks to implement
 * role-based or permission-based access control mechanisms.
 * <p>
 * The 'permission' field
 * stores the string representation of the permission, which can be used in
 * annotations, database storage, or any other context where permissions need to be
 * represented as strings.
 */
public enum Permission {
    DEVELOPERS_READ("developers:read"),
    DEVELOPERS_WRITE("developers:write");
    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
