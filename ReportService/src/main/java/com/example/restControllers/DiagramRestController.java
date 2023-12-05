package com.example.restControllers;

import com.example.dto.MovementMoneyDTO;
import com.example.models.DiagramData;
import com.example.services.DiagramService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This controller provides an API endpoint for generating diagram data based on financial transactions.
 */
@RestController
@RequestMapping("/api/v1/diagram")
public class DiagramRestController {
    /**
     * DiagramService used for generating diagram data.
     */
    private final DiagramService diagramService;

    public DiagramRestController(DiagramService diagramService) {
        this.diagramService = diagramService;
    }

    /**
     * This endpoint processes a POST request that contains a list of MovementMoneyDTO objects. These objects
     * represent financial transactions. The method uses these transactions to generate data suitable for
     * diagrammatic representations, such as graphs or charts.
     * <p>
     * Process:
     * - Receives a list of MovementMoneyDTO objects through the request body.
     * - Invokes the DiagramService's getDataDiagram method with the provided list to generate diagram data.
     * - The service returns a list of DiagramData objects, each containing information like date and balance,
     * suitable for visualization.
     *
     * @param moneyDTOList The list of MovementMoneyDTO objects used for generating diagram data.
     * @return A ResponseEntity containing a list of DiagramData objects for visualization.
     */
    @PostMapping
    public ResponseEntity<List<DiagramData>> getDiagramData(@RequestBody List<MovementMoneyDTO> moneyDTOList) {

        List<DiagramData> reportDiagram = diagramService.getDataDiagram(moneyDTOList);

        return ResponseEntity.ok(reportDiagram);
    }
}
