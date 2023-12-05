package com.example.restControllers;

import com.example.dto.MovementMoneyDTO;
import com.example.models.MovementMoney;
import com.example.services.MovementMoneyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This controller handles the API endpoints related to managing financial movements such as adding,
 * updating, deleting, and retrieving transactions.
 */
@RestController
@RequestMapping("/api/v1/mm")
public class MovementMoneyRestController {

    /**
     * MovementMoneyService responsible for handling operations related to financial transactions.
     */
    private final MovementMoneyService movementMoneyService;

    public MovementMoneyRestController(MovementMoneyService movementMoneyService) {
        this.movementMoneyService = movementMoneyService;
    }

    /**
     * This endpoint handles POST requests to add a new expense or income record. It takes a MovementMoneyDTO
     * object containing the transaction details and uses the MovementMoneyService to add the transaction
     * to the database.
     *
     * @param movementMoneyDTO The DTO containing data for the new transaction.
     * @return A ResponseEntity containing the added MovementMoney entity.
     */
    @PostMapping
    public ResponseEntity<MovementMoney> addExpense(@RequestBody MovementMoneyDTO movementMoneyDTO) {
        return ResponseEntity.ok(movementMoneyService.addMovementMoney(movementMoneyDTO));
    }

    /**
     * This endpoint handles PUT requests for updating a specific transaction, identified by its ID. It accepts
     * a MovementMoneyDTO containing the updated transaction details and updates the corresponding record in the database.
     *
     * @param id               The ID of the transaction to be updated.
     * @param movementMoneyDTO The DTO with updated data for the transaction.
     * @return A ResponseEntity containing the updated MovementMoney entity.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MovementMoney> updateExpense(@PathVariable Long id,
                                                       @RequestBody MovementMoneyDTO movementMoneyDTO) {
        return ResponseEntity.ok(movementMoneyService.updateMovementMoney(id, movementMoneyDTO));
    }

    /**
     * This endpoint handles DELETE requests to remove a specific transaction from the database,
     * identified by its ID. It invokes the MovementMoneyService to perform the deletion operation.
     *
     * @param id The ID of the transaction to be deleted.
     */
    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable Long id) {
        movementMoneyService.deleteMovementMoney(id);
    }

    /**
     * This endpoint handles GET requests to obtain a list of all financial movements (incomes and expenses)
     * associated with a user's login. It utilizes MovementMoneyService to fetch the transaction data.
     *
     * @param login The login identifier of the user whose transactions are being requested.
     * @return A ResponseEntity containing a list of MovementMoney entities for the specified user.
     */
    @GetMapping("/{login}")
    public ResponseEntity<List<MovementMoney>> getAllMoveMoney(@PathVariable String login) {
        return ResponseEntity.ok(movementMoneyService.getAllMovementMoneyByUserLogin(login));
    }
}
