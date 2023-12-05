package com.example.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for user registration requests.
 * <p>
 * This class encapsulates the data required for registering a new user.
 * The DTO pattern is used here to transfer the necessary data from the client
 * to the server, specifically for user registration processes.
 * <p>
 * The @Data annotation from the Lombok library is used to automatically generate
 * boilerplate code such as getters, setters, equals, hashCode, and toString methods.
 */
@Data
public class RegistrationRequestDTO {
    private String login;
    private String password;
    private String role = "USER";
    private String status = "ACTIVE";
}
