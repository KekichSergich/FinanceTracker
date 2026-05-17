package application.service;

import domain.model.Category;
import domain.model.Transaction;
import domain.repository.TransactionRepository;
import util.AppLogger;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

public class TransactionService {

    private static final Logger logger = AppLogger.get(TransactionService.class);

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Saves a new transaction.
     * Throws IllegalArgumentException if amount is zero or negative.
     */
    public void addTransaction(Transaction transaction) {
        if (transaction.getAmount() > 0) {
            transactionRepository.save(transaction);
            logger.info("Transaction added: type=" + transaction.getType() + " amount=" + transaction.getAmount() + " category=" + transaction.getCategory());
        } else {
            logger.warning("Rejected transaction with invalid amount: " + transaction.getAmount());
            throw new IllegalArgumentException("Transaction amount cannot be 0");
        }
    }

    /**
     * Updates an existing transaction by its ID.
     */
    public void updateTransaction(Transaction transaction) {
        transactionRepository.update(transaction);
        logger.info("Transaction updated: id=" + transaction.getId());
    }

    /**
     * Deletes a transaction by its ID.
     */
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
        logger.info("Transaction deleted: id=" + id);
    }

    /**
     * Returns all transactions from the repository.
     */
    public List<Transaction> getAllTransactions() {
        List<Transaction> result = transactionRepository.findAll();
        logger.fine("Fetched all transactions: count=" + result.size());
        return result;
    }

    /**
     * Returns transactions filtered by category.
     */
    public List<Transaction> getTransactionsByCategory(Category category) {
        List<Transaction> result = transactionRepository.findByCategory(category);
        logger.fine("Fetched transactions by category=" + category + " count=" + result.size());
        return result;
    }

    /**
     * Returns transactions within the given date range (inclusive).
     */
    public List<Transaction> getTransactionsByDateRange(LocalDate from, LocalDate to) {
        List<Transaction> result = transactionRepository.findByDateRange(from, to);
        logger.fine("Fetched transactions from=" + from + " to=" + to + " count=" + result.size());
        return result;
    }
}