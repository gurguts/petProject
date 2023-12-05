package com.example.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

/**
 * This class is an entity model that maps to the "movement_money" table in the database. It is used
 * to store and retrieve data related to financial transactions or money movements, such as expenses and
 * incomes. Each instance of MovementMoney represents a single transaction or movement record.
 * <p>
 * Annotations:
 * - @Data: Lombok annotation to automatically generate getters, setters, equals, hashCode, and toString methods.
 * - @Entity: Specifies that this class is an entity and is mapped to a database table.
 * - @Table(name = "movement_money"): Defines the specific table in the database to which this entity is mapped.
 */
@Data
@Entity
@Table(name = "movement_money")
public class MovementMoney {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "description")
    private String description;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private TypeMovement type;
}

