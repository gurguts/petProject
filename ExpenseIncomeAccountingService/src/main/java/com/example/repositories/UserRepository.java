package com.example.repositories;

import com.example.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * This interface extends JpaRepository, offering a range of standard database operations for User entities,
 * as well as additional custom methods tailored to user data. It manages interactions with the 'users' table
 * in the database.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * This method is part of the UserRepository interface and is used to verify the presence of a user
     * based on their login name. It returns a boolean value indicating whether a user with the given login
     * exists in the 'users' table of the database.
     *
     * @param login The login name to check in the database.
     * @return A boolean value: true if a user with the specified login exists, false otherwise.
     */
    /*@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.login = :login")*/
    boolean existsByLogin(String login);

    /**
     * This method is a part of the UserRepository interface and is designed to find a user in the database
     * based on their login name. It returns an Optional<User> which encapsulates the result of the query.
     * <p>
     * The use of Optional<User> as the return type serves several purposes:
     * it eliminates the need for null checks, making the code more robust and less prone to null pointer exceptions.
     *
     * @param login The login name of the user to be retrieved.
     * @return An Optional containing the User if found, or an empty Optional if no user is found with the given login.
     */
    /*@Query("SELECT u FROM User u WHERE u.login = :login")*/
    Optional<User> findByLogin(String login);
}
