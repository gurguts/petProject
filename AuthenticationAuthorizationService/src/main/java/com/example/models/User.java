package com.example.models;

import lombok.Data;

import jakarta.persistence.*;

/**
 * This class models the user entity with its core attributes and maps it to the corresponding
 * database table named 'users'.
 * <p>
 * The class is annotated with @Data from Lombok to automatically generate boilerplate
 * code like getters, setters, equals, hashCode, and toString methods.
 * <p>
 * Annotations:
 * - @Data: Generates getters, setters, and other utility methods.
 * - @Entity: Marks this class as a JPA entity that will be mapped to a database table.
 * - @Table(name = "users"): Specifies the table in the database to which this entity is mapped.
 */
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "login")
    private String login;
    @Column(name = "password")
    private String password;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status;
}
