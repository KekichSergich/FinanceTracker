package application.service;

import domain.model.Category;
import domain.model.Transaction;
import domain.repository.TransactionRepository;

import java.time.LocalDate;
import java.util.List;

public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void addTransaction(Transaction transaction) {
        // TODO
    }

    public void updateTransaction(Transaction transaction) {
        // TODO
    }

    public void deleteTransaction(Long id) {
        // TODO
    }

    public List<Transaction> getAllTransactions() {
        // TODO
        return null;
    }

    public List<Transaction> getTransactionsByCategory(Category category) {
        // TODO
        return null;
    }

    public List<Transaction> getTransactionsByDateRange(LocalDate from, LocalDate to) {
        // TODO
        return null;
    }
}