package com.example.repository;

import com.example.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository interface for User entities.
 * <p>
 * This interface extends JpaRepository, providing a range of standard methods for
 * performing database operations on User entities, such as CRUD (Create, Read, Update,
 * Delete) operations. It also includes custom query methods tailored for User entities.
 * <p>
 * The repository leverages Spring Data's capability to generate query implementations
 * automatically based on method names, thus reducing the need for boilerplate code.
 * <p>
 * The use of Optional in query methods follows the best practices for handling potential
 * absence of values, providing a safer alternative to returning null.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * This method provides the functionality to retrieve a User entity from the database
     * based on the provided login value. The use of Optional as the return type indicates
     * that the method might not always find a matching User entity. In cases where no user
     * is found with the given login, an empty Optional is returned instead of a null,
     * thereby helping to avoid NullPointerExceptions and simplify null checks in the code.
     * <p>
     * I commented out the @Query annotation because the method works without it.
     * But I read in a book that it is always better to write using @Query.
     *
     * @param login The login identifier of the user to be searched for.
     * @return An Optional containing the User entity if found, or an empty Optional if no
     * user with the provided login exists in the database.
     */
    /*@Query("SELECT * FROM users WHERE login = :login")*/
    Optional<User> findByLogin(String login);

    /**
     * This method is used to determine whether there is a user in the database with the given login.
     * The method returns a boolean value - 'true' if a user with the specified login exists, and 'false' otherwise.
     * <p>
     * I commented out the @Query annotation because the method works without it.
     * But I read in a book that it is always better to write using @Query.
     *
     * @param login The login identifier to be checked in the database.
     * @return A boolean value indicating the existence of a user with the given login.
     */
    /*@Query("SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM users WHERE login = :login")*/
    boolean existsByLogin(String login);
}
