package com.example.models;

import jakarta.persistence.*;
import lombok.Data;

/**
 * This class is an entity model for user data and is mapped to the "users" table in the database.
 * Each instance of User represents a unique user with specific attributes.
 * <p>
 * Annotations:
 * - @Data: Lombok annotation to automatically generate getters, setters, equals, hashCode, and toString methods.
 * - @Entity: Specifies that this class is an entity and is mapped to a database table.
 * - @Table(name = "users"): Defines the specific table in the database to which this entity is mapped.
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
}
