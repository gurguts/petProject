package com.example.restControllers;

import com.example.dto.MovementMoneyDTO;
import com.example.services.BalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This controller handles API endpoints related to calculating and retrieving balance information based
 * on financial transactions.
 */
@RestController
@RequestMapping("/api/v1/balance")
public class BalanceRestController {

    /**
     * BalanceService used for balance calculation operations.
     */
    private final BalanceService balanceService;

    public BalanceRestController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    /**
     * This endpoint handles POST requests to calculate the total balance based on a provided list of
     * MovementMoneyDTO objects. Each DTO represents an individual financial transaction, and the
     * collective balance is computed from these transactions.
     * <p>
     * Steps:
     * - Receives a list of MovementMoneyDTO objects in the request body.
     * - Calls the BalanceService's calculateBalance method, passing the list of DTOs.
     * - The BalanceService processes the list and returns the calculated balance.
     * <p>
     * The method returns a ResponseEntity with an OK status containing the calculated balance,
     * allowing clients to retrieve an up-to-date balance based on dynamic transaction data.
     *
     * @param moneyDTOList The list of MovementMoneyDTO objects for balance calculation.
     * @return A ResponseEntity with the calculated balance.
     */
    @PostMapping
    public ResponseEntity<?> getBalance(@RequestBody List<MovementMoneyDTO> moneyDTOList) {
        double balance = balanceService.calculateBalance(moneyDTOList);

        return ResponseEntity.ok(balance);
    }
}
