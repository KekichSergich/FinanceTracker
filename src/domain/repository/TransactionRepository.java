package domain.repository;

import domain.model.Category;
import domain.model.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface responsible for managing transaction persistence.
 * Provides CRUD operations and search methods for transaction entities.
 */
public interface TransactionRepository {

    /**
     * Saves a new transaction.
     *
     * @param transaction transaction to be stored
     */
    void save(Transaction transaction);

    /**
     * Updates an existing transaction.
     *
     * @param transaction transaction with updated values
     */
    void update(Transaction transaction);

    /**
     * Deletes a transaction by its unique identifier.
     *
     * @param id transaction identifier
     */
    void deleteById(Long id);

    /**
     * Finds a transaction by its unique identifier.
     *
     * @param id transaction identifier
     * @return optional containing transaction if found,
     *         otherwise empty
     */
    Optional<Transaction> findById(Long id);

    /**
     * Returns all stored transactions.
     *
     * @return list of all transactions
     */
    List<Transaction> findAll();

    /**
     * Returns transactions belonging to a specific category.
     *
     * @param category transaction category
     * @return list of matching transactions
     */
    List<Transaction> findByCategory(Category category);

    /**
     * Returns transactions within a date interval.
     *
     * @param from start date
     * @param to end date
     * @return list of matching transactions
     */
    List<Transaction> findByDateRange(LocalDate from, LocalDate to);

    /**
     * Removes all stored transactions.
     */
    void deleteAll();

    /**
     * Saves multiple transactions at once.
     *
     * @param transactions list of transactions to store
     */
    void saveAll(List<Transaction> transactions);
}