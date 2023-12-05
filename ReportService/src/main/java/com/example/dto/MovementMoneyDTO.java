package com.example.dto;

import com.example.models.TypeMovement;
import lombok.Data;

import java.util.Date;

/**
 * This class is used to transfer financial transaction data between different layers of the application,
 * particularly from clients to the server. It encapsulates all the necessary information for creating or
 * updating financial records, such as expenses or incomes.
 */
@Data
public class MovementMoneyDTO {
    private String login;
    private String description;
    private Double amount;
    private Date date;
    private TypeMovement type;
}
