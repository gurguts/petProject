package com.example.dto;

import lombok.Data;

import java.util.Date;

/**
 * Data Transfer Object (DTO) for representing diagram data.
 * <p>
 * The @Data annotation from Lombok is used to automatically generate boilerplate code such as getters,
 * setters, equals, hashCode, and toString methods, thereby simplifying the class structure and reducing
 * manual coding.
 */
@Data
public class DiagramDataDTO {
    private Date date;
    private Double balance;
}
