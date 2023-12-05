package com.example.models;

import lombok.Data;

import java.util.Date;

/**
 * This class is designed to encapsulate data used in diagrams or visual representations of financial information.
 * It primarily focuses on holding data points that are relevant for plotting or displaying in graphical formats
 * such as charts or graphs.
 */
@Data
public class DiagramData {
    private Date date;
    private Double balance;
}
