package com.example.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for authentication requests.
 * <p>
 * This class is used to encapsulate the data required for user authentication. As a DTO, it serves the purpose
 * of structuring the data for transfer, particularly in the context of processing HTTP requests.
 * <p>
 * The @Data annotation from Lombok automatically generates boilerplate code
 * such as getters, setters, equals, hashCode, and toString methods, reducing
 * the need for manually writing this common code.
 */
@Data
public class AuthenticationRequestDTO {
    private String login;
    private String password;
}
