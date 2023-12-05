package com.example.dto;

import com.example.models.TypeMovement;
import lombok.Data;

import java.util.Date;

/**
 * Data Transfer Object (DTO) for movement of money-related information.
 * <p>
 * The @Data annotation from Lombok automatically generates essential boilerplate code such as getters,
 * setters, equals, hashCode, and toString methods, simplifying the class structure and maintenance.
 */
@Data
public class MovementMoneyDTO {
    private String login;
    private String description;
    private Double amount;
    private Date date;
    private TypeMovement type;
}
