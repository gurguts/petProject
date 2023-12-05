package com.example.services;

import com.example.dto.MovementMoneyDTO;
import com.example.exception.MovementMoneyNotFoundException;
import com.example.models.MovementMoney;
import com.example.models.User;
import com.example.repositories.MovementMoneyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This service provides functionalities related to the management of financial movements such as
 * adding, updating, deleting, and retrieving transaction records.
 * <p>
 * All transactional methods are annotated with @Transactional, ensuring proper management of database
 * transactions during execution.
 */
@Service
public class MovementMoneyService {
    /**
     * MovementMoneyRepository is used for database operations
     */
    private final MovementMoneyRepository movementMoneyRepository;
    /**
     * UserService is used for user related information
     */
    private final UserService userService;

    public MovementMoneyService(MovementMoneyRepository movementMoneyRepository, UserService userService) {
        this.movementMoneyRepository = movementMoneyRepository;
        this.userService = userService;
    }

    /**
     * This method, marked as @Transactional, ensures the entire process is executed within a transactional
     * context. It creates and stores a new MovementMoney entity based on the provided MovementMoneyDTO.
     * <p>
     * Steps:
     * - Creates a new MovementMoney entity.
     * - Fetches the associated User entity using the login from the DTO.
     * - Sets the MovementMoney entity's properties (user, description, amount, date, and type) from the DTO.
     * - Saves the entity to the database using MovementMoneyRepository.
     * <p>
     * The method returns the saved MovementMoney entity, which includes the generated ID and other persisted data.
     *
     * @param movementMoneyDTO The data transfer object containing details of the financial transaction.
     * @return The saved MovementMoney entity with updated information.
     */
    @Transactional
    public MovementMoney addMovementMoney(MovementMoneyDTO movementMoneyDTO) {
        MovementMoney movementMoney = new MovementMoney();
        User foundUser = userService.getUserByLogin(movementMoneyDTO.getLogin());

        movementMoney.setUser(foundUser);
        movementMoney.setDescription(movementMoneyDTO.getDescription());
        movementMoney.setAmount(movementMoneyDTO.getAmount());
        movementMoney.setDate(movementMoneyDTO.getDate());
        movementMoney.setType(movementMoneyDTO.getType());
        return movementMoneyRepository.save(movementMoney);
    }

    /**
     * This @Transactional method updates a MovementMoney entity based on the provided ID and data from
     * the MovementMoneyDTO. The method ensures that the entire operation occurs within a transactional context.
     * <p>
     * Process:
     * - Retrieves the existing MovementMoney entity by ID. Throws MovementMoneyNotFoundException if not found.
     * - Fetches the associated User entity using the login from the DTO.
     * - Updates the MovementMoney entity's properties (description, amount, date, and user) with data from the DTO.
     * - Saves the updated entity to the database using MovementMoneyRepository.
     * <p>
     * The method returns the updated MovementMoney entity with the latest changes persisted.
     *
     * @param id               The ID of the transaction to be updated.
     * @param movementMoneyDTO The data transfer object containing the updated transaction details.
     * @return The updated MovementMoney entity.
     */
    @Transactional
    public MovementMoney updateMovementMoney(Long id, MovementMoneyDTO movementMoneyDTO) {
        MovementMoney movementMoney = movementMoneyRepository.findById(id)
                .orElseThrow(() -> new MovementMoneyNotFoundException("Expense not found with id " + id));

        User foundUser = userService.getUserByLogin(movementMoneyDTO.getLogin());

        movementMoney.setDescription(movementMoneyDTO.getDescription());
        movementMoney.setAmount(movementMoneyDTO.getAmount());
        movementMoney.setDate(movementMoneyDTO.getDate());
        movementMoney.setUser(foundUser);

        return movementMoneyRepository.save(movementMoney);
    }

    /**
     * This method, marked as @Transactional, ensures the deletion operation is executed within a transactional context.
     * It removes a MovementMoney entity from the database based on the provided ID.
     * <p>
     * Process:
     * - Checks if the provided ID is not null.
     * - If the ID is valid, it uses the MovementMoneyRepository to delete the corresponding entity.
     * - The deletion is skipped if the ID is null to prevent unintended operations.
     *
     * @param id The ID of the transaction to be deleted.
     */
    @Transactional
    public void deleteMovementMoney(Long id) {
        if (id != null) {
            movementMoneyRepository.deleteById(id);
        }
    }

    /**
     * This method fetches a list of MovementMoney entities associated with a user, identified by their login.
     * It is used to gather a comprehensive record of all financial movements (incomes and expenses) for a user.
     * <p>
     * Process:
     * - Obtains the user's ID from the UserService using the provided login.
     * - Utilizes MovementMoneyRepository to find all transactions linked to the user's ID.
     *
     * @param login The login identifier of the user whose financial transactions are being requested.
     * @return A list of MovementMoney entities associated with the user's ID.
     */
    public List<MovementMoney> getAllMovementMoneyByUserLogin(String login) {
        Long userId = userService.getId(login);
        return movementMoneyRepository.findByUserId(userId);
    }
}
