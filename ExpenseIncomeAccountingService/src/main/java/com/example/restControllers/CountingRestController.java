package com.example.restControllers;

import com.example.dto.DiagramDataDTO;
import com.example.models.MovementMoney;
import com.example.services.MovementMoneyService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * This controller provides endpoints for accessing and manipulating financial data such as
 * user balances and diagram data.
 */
@RestController
@RequestMapping("/api/v1/counting")
public class CountingRestController {

    /**
     * MovementMoneyService is used to receive data
     */
    private final MovementMoneyService movementMoneyService;

    /**
     * RestTemplate is used to send HTTP requests to external microservices.
     */
    private final RestTemplate restTemplate;

    public CountingRestController(MovementMoneyService movementMoneyService, RestTemplate restTemplate) {
        this.movementMoneyService = movementMoneyService;
        this.restTemplate = restTemplate;
    }

    /**
     * This method handles GET requests to the "/balance/{login}" URL, where "{login}" is a variable
     * representing the login of the user whose balance is to be calculated.
     * <p>
     * Process:
     * - The user's financial movements are retrieved based on their login using MovementMoneyService.
     * - These financial data are sent to an external service
     * (at "<a href="http://localhost:8082/api/v1/balance">...</a>") using a POST request via RestTemplate.
     * - The external service is expected to return the calculated balance, which is then sent back as
     * the response of this endpoint.
     * <p>
     * Exception handling:
     * - If any exception occurs during the process, the method returns an Internal Server Error response,
     * indicating a failure in processing the request.
     *
     * @param login The login identifier of the user whose balance is to be calculated.
     * @return A ResponseEntity containing the calculated balance or an error status in case of failure.
     */
    @GetMapping("/balance/{login}")
    public ResponseEntity<Double> getBalance(@PathVariable String login) {
        try {
            List<MovementMoney> moneyList = movementMoneyService.getAllMovementMoneyByUserLogin(login);

            return restTemplate.postForEntity(
                    "http://localhost:8082/api/v1/balance",
                    moneyList,
                    Double.class);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * This method handles GET requests to the "/diagram/{login}" URL, where "{login}" is the variable
     * path segment representing the login of the user.
     * <p>
     * Process:
     * - Retrieves a list of MovementMoney entities associated with the user's login using MovementMoneyService.
     * - The list of financial movements is sent to an external service
     * (at "<a href="http://localhost:8082/api/v1/diagram">...</a>") using a POST request via RestTemplate.
     * The request aims to receive diagram data based on these financial movements.
     * - The response from the external service, expected to be a list of DiagramDataDTO objects, is returned as
     * the response of this endpoint.
     * <p>
     * Exception handling:
     * - In case of any exceptions during the processing, the method returns an Internal Server Error response,
     * indicating a failure in retrieving the diagram data.
     *
     * @param login The login identifier of the user whose diagram data is to be retrieved.
     * @return A ResponseEntity containing a list of DiagramDataDTO objects or an error status in case of failure.
     */
    @GetMapping("/diagram/{login}")
    public ResponseEntity<List<DiagramDataDTO>> getDiagramData(@PathVariable String login) {
        try {
            List<MovementMoney> moneyList = movementMoneyService.getAllMovementMoneyByUserLogin(login);

            ParameterizedTypeReference<List<DiagramDataDTO>> typeRef = new ParameterizedTypeReference<>() {
            };

            return restTemplate.exchange(
                    "http://localhost:8082/api/v1/diagram",
                    HttpMethod.POST,
                    new HttpEntity<>(moneyList),
                    typeRef);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
