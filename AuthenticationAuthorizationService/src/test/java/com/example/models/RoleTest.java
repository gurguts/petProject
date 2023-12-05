package com.example.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public class RoleTest {

    @Test
    public void roleUserShouldHaveCorrectPermissions() {
        Set<Permission> expectedPermissions = Set.of(Permission.DEVELOPERS_READ);
        assertEquals(expectedPermissions, Role.USER.getPermissionSet());
    }

    @Test
    public void roleAdminShouldHaveCorrectPermissions() {
        Set<Permission> expectedPermissions = Set.of(Permission.DEVELOPERS_READ, Permission.DEVELOPERS_WRITE);
        assertEquals(expectedPermissions, Role.ADMIN.getPermissionSet());
    }

    @Test
    public void roleUserShouldHaveCorrectAuthorities() {
        Set<SimpleGrantedAuthority> expectedAuthorities = Role.USER.getPermissionSet().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        assertEquals(expectedAuthorities, Role.USER.getAuthorities());
    }

    @Test
    public void roleAdminShouldHaveCorrectAuthorities() {
        Set<SimpleGrantedAuthority> expectedAuthorities = Role.ADMIN.getPermissionSet().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        assertEquals(expectedAuthorities, Role.ADMIN.getAuthorities());
    }
}

