package com.example.repositories;

import com.example.models.MovementMoney;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository interface for MovementMoney entity.
 * <p>
 * This interface extends CrudRepository, providing CRUD (Create, Read, Update, Delete) operations
 * for MovementMoney entities along with additional custom methods specific to MovementMoney data.
 * It interacts with the database primarily dealing with the 'movement_money' table.
 */
public interface MovementMoneyRepository extends CrudRepository<MovementMoney, Long> {
    /**
     * This method is defined in the MovementMoneyRepository interface and is used to fetch all
     * financial transaction records that are linked to a particular user, identified by their user ID.
     *
     * @param userId The ID of the user whose financial transactions are to be retrieved.
     * @return A list of MovementMoney entities associated with the given user ID.
     */
    /*@Query(value = "SELECT * FROM movement_money WHERE user_id = :userId")*/
    List<MovementMoney> findByUserId(Long userId);
}
