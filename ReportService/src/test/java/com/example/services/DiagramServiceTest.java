package com.example.services;

import com.example.dto.MovementMoneyDTO;
import com.example.models.DiagramData;
import com.example.models.TypeMovement;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DiagramServiceTest {
    private final DiagramService diagramService = new DiagramService();

    @Test
    public void getDataDiagramListIsNull() {
        assertTrue(diagramService.getDataDiagram(null).isEmpty());
    }

    @Test
    public void getDataDiagramListIsEmpty() {
        assertTrue(diagramService.getDataDiagram(Collections.emptyList()).isEmpty());
    }

    @Test
    public void getDataDiagramMultipleEntriesInSingleMonth() {
        MovementMoneyDTO movementMoneyDTO1 = new MovementMoneyDTO();
        movementMoneyDTO1.setDate(new Date());
        movementMoneyDTO1.setAmount(100.0);
        movementMoneyDTO1.setType(TypeMovement.INCOME);
        MovementMoneyDTO movementMoneyDTO2 = new MovementMoneyDTO();
        movementMoneyDTO2.setDate(new Date());
        movementMoneyDTO2.setAmount(200.0);
        movementMoneyDTO2.setType(TypeMovement.EXPENSE);
        List<MovementMoneyDTO> moneyDTOList = Arrays.asList(
                movementMoneyDTO1,
                movementMoneyDTO2
        );
        List<DiagramData> result = diagramService.getDataDiagram(moneyDTOList);
        assertEquals(1, result.size());
        assertEquals(-100.0, result.get(0).getBalance());
    }

    @Test
    public void getDataDiagramEntriesInDifferentMonths() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        MovementMoneyDTO dto1 = new MovementMoneyDTO();
        dto1.setDate(formatter.parse("2023-01-15"));
        dto1.setAmount(100.0);
        dto1.setType(TypeMovement.INCOME);
        MovementMoneyDTO dto2 = new MovementMoneyDTO();
        dto2.setDate(formatter.parse("2023-02-10"));
        dto2.setAmount(50.0);
        dto2.setType(TypeMovement.EXPENSE);
        MovementMoneyDTO dto3 = new MovementMoneyDTO();
        dto3.setDate(formatter.parse("2023-01-20"));
        dto3.setAmount(200.0);
        dto3.setType(TypeMovement.EXPENSE);
        List<MovementMoneyDTO> moneyDTOList = Arrays.asList(dto1, dto2, dto3);

        List<DiagramData> result = diagramService.getDataDiagram(moneyDTOList);

        assertEquals(2, result.size());

        DiagramData januaryData = result.stream()
                .filter(data -> {
                    try {
                        return data.getDate().equals(formatter.parse("2023-01-01"));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                })
                .findFirst()
                .orElseThrow();
        assertEquals(-100.0, januaryData.getBalance());


        DiagramData februaryData = result.stream()
                .filter(data -> {
                    try {
                        return data.getDate().equals(formatter.parse("2023-02-01"));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                })
                .findFirst()
                .orElseThrow();
        assertEquals(-50.0, februaryData.getBalance());
    }

    @Test
    public void getDataDiagramChronologicalOrder() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        MovementMoneyDTO dto1 = new MovementMoneyDTO();
        dto1.setDate(formatter.parse("2023-01-10"));
        dto1.setAmount(100.0);
        dto1.setType(TypeMovement.INCOME);

        MovementMoneyDTO dto2 = new MovementMoneyDTO();
        dto2.setDate(formatter.parse("2023-03-15"));
        dto2.setAmount(200.0);
        dto2.setType(TypeMovement.EXPENSE);

        MovementMoneyDTO dto3 = new MovementMoneyDTO();
        dto3.setDate(formatter.parse("2023-02-20"));
        dto3.setAmount(300.0);
        dto3.setType(TypeMovement.INCOME);

        List<MovementMoneyDTO> moneyDTOList = Arrays.asList(dto1, dto2, dto3);
        List<DiagramData> result = diagramService.getDataDiagram(moneyDTOList);

        assertEquals(3, result.size());
        assertEquals(formatter.parse("2023-01-01"), result.get(0).getDate());
        assertEquals(formatter.parse("2023-02-01"), result.get(1).getDate());
        assertEquals(formatter.parse("2023-03-01"), result.get(2).getDate());
    }

    @Test
    public void getDataDiagramZeroAndNegativeAmounts() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        MovementMoneyDTO dto1 = new MovementMoneyDTO();
        dto1.setDate(formatter.parse("2023-01-10"));
        dto1.setAmount(0.0); // Нулевая сумма
        dto1.setType(TypeMovement.INCOME);

        MovementMoneyDTO dto2 = new MovementMoneyDTO();
        dto2.setDate(formatter.parse("2023-01-15"));
        dto2.setAmount(-100.0);
        dto2.setType(TypeMovement.EXPENSE);

        MovementMoneyDTO dto3 = new MovementMoneyDTO();
        dto3.setDate(formatter.parse("2023-01-20"));
        dto3.setAmount(50.0);
        dto3.setType(TypeMovement.EXPENSE);

        List<MovementMoneyDTO> moneyDTOList = Arrays.asList(dto1, dto2, dto3);

        List<DiagramData> result = diagramService.getDataDiagram(moneyDTOList);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        DiagramData data = result.get(0);
        assertEquals(formatter.parse("2023-01-01"), data.getDate());
        assertEquals(50.0, data.getBalance());
    }

    @Test
    public void getDataDiagramMonthAcrossDifferentYears() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        MovementMoneyDTO dto1 = new MovementMoneyDTO();
        dto1.setDate(formatter.parse("2022-01-15"));
        dto1.setAmount(100.0);
        dto1.setType(TypeMovement.INCOME);

        MovementMoneyDTO dto2 = new MovementMoneyDTO();
        dto2.setDate(formatter.parse("2023-01-10"));
        dto2.setAmount(50.0);
        dto2.setType(TypeMovement.EXPENSE);

        MovementMoneyDTO dto3 = new MovementMoneyDTO();
        dto3.setDate(formatter.parse("2022-01-20"));
        dto3.setAmount(200.0);
        dto3.setType(TypeMovement.EXPENSE);

        List<MovementMoneyDTO> moneyDTOList = Arrays.asList(dto1, dto2, dto3);

        List<DiagramData> result = diagramService.getDataDiagram(moneyDTOList);

        assertEquals(2, result.size());

        DiagramData jan2022Data = result.stream()
                .filter(data -> {
                    try {
                        return data.getDate().equals(formatter.parse("2022-01-01"));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                })
                .findFirst()
                .orElseThrow();
        assertEquals(-100.0, jan2022Data.getBalance());

        DiagramData jan2023Data = result.stream()
                .filter(data -> {
                    try {
                        return data.getDate().equals(formatter.parse("2023-01-01"));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                })
                .findFirst()
                .orElseThrow();
        assertEquals(-50.0, jan2023Data.getBalance());
    }

    @Test
    public void getDataDiagramLargeDataSet() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        List<MovementMoneyDTO> largeDataSet = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 1000; i++) {
            MovementMoneyDTO dto = new MovementMoneyDTO();

            String dateStr =
                    String.format("2023-%02d-%02d", 1 + random.nextInt(12), 1 + random.nextInt(28));
            dto.setDate(formatter.parse(dateStr));

            dto.setAmount(random.nextDouble() * 1000);
            dto.setType(random.nextBoolean() ? TypeMovement.INCOME : TypeMovement.EXPENSE);
            largeDataSet.add(dto);
        }

        List<DiagramData> result = diagramService.getDataDiagram(largeDataSet);

        Set<Date> uniqueMonths = new HashSet<>();
        for (DiagramData data : result) {
            uniqueMonths.add(data.getDate());
        }

        assertTrue(uniqueMonths.size() <= 12);
        assertEquals(result.size(), uniqueMonths.size());
    }
}
