package com.example.services;

import com.example.dto.MovementMoneyDTO;
import com.example.models.DiagramData;
import com.example.models.TypeMovement;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This service processes a list of MovementMoneyDTO objects and aggregates their data to produce
 * information suitable for visualization in diagrams, particularly focusing on the balance changes over time.
 */
@Service
public class DiagramService {

    /**
     * This method processes a list of MovementMoneyDTO objects to create a chronological series of DiagramData.
     * Each DiagramData object represents the balance for a specific month and year, allowing for the visualization
     * of financial trends over time.
     * <p>
     * Process:
     * - Checks if the provided list is null, returning an empty list to handle null inputs safely.
     * - Aggregates the transactions by month and year, calculating the net balance for each period.
     * - Maps each aggregated entry to a DiagramData object, setting the corresponding date and balance.
     * - Sorts the resulting list chronologically by date.
     *
     * @param moneyDTOList The list of MovementMoneyDTO objects used for generating diagram data.
     * @return A sorted list of DiagramData objects for each month and year.
     */
    public List<DiagramData> getDataDiagram(List<MovementMoneyDTO> moneyDTOList) {
        if (moneyDTOList == null) {
            return Collections.emptyList();
        }

        Map<String, BigDecimal> balancePerMonth = moneyDTOList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        dto -> getMonthYearKey(dto.getDate()),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                dto -> BigDecimal.valueOf(dto.getAmount())
                                        .multiply(dto.getType() == TypeMovement.INCOME ?
                                                BigDecimal.ONE : BigDecimal.valueOf(-1)),
                                BigDecimal::add)));

        return balancePerMonth.entrySet().stream()
                .map(this::createDiagramData)
                .sorted(Comparator.comparing(DiagramData::getDate))
                .collect(Collectors.toList());
    }

    /**
     * This private helper method transforms a single entry from a map (where each entry represents a month-year
     * key and a corresponding balance) into a DiagramData object. The DiagramData object is used for visualizing
     * financial data in diagrams or charts.
     * <p>
     * Process:
     * - Creates a new DiagramData object.
     * - Parses the month-year key to a Date object and sets it as the date of the DiagramData.
     * - Sets the balance (extracted from the map entry's value) in the DiagramData object.
     *
     * @param entry A map entry where the key is a month-year string and the value is the balance for that period.
     * @return A DiagramData object representing the financial data for the given period.
     */
    private DiagramData createDiagramData(Map.Entry<String, BigDecimal> entry) {
        DiagramData data = new DiagramData();
        data.setDate(parseDate(entry.getKey()));
        data.setBalance(entry.getValue().doubleValue());
        return data;
    }

    /**
     * This private helper method takes a Date object and converts it into a string formatted as "yyyy-MM".
     * The formatted string represents the year and month of the given date, serving as a key for grouping
     * financial data in monthly intervals.
     *
     * @param date The Date object to be formatted into a month-year key.
     * @return A string representing the year and month of the given date, formatted as "yyyy-MM".
     */
    private String getMonthYearKey(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        return formatter.format(date);
    }

    /**
     * This private method takes a string representing a year and month (formatted as "yyyy-MM") and converts
     * it into a Date object. It uses SimpleDateFormat for parsing the string based on the specified format.
     * <p>
     * If the string is not in the correct format, the method throws an IllegalArgumentException, indicating
     * that the provided string does not conform to the expected date format.
     *
     * @param monthYear A string representing the year and month, formatted as "yyyy-MM".
     * @return A Date object corresponding to the given month-year string.
     * @throws IllegalArgumentException if the string cannot be parsed into a valid Date object.
     */
    private Date parseDate(String monthYear) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        try {
            return formatter.parse(monthYear);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + monthYear, e);
        }
    }
}
