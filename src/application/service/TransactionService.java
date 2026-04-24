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
        if (transaction.getAmount() > 0){
            transactionRepository.save(transaction);
        }
        else { throw new IllegalArgumentException("Transaction amount cannot be 0"); }
    }

    public void updateTransaction(Transaction transaction) {
        transactionRepository.update(transaction);
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsByCategory(Category category) {
        return transactionRepository.findByCategory(category);
    }

    public List<Transaction> getTransactionsByDateRange(LocalDate from, LocalDate to) {
        return transactionRepository.findByDateRange(from, to);
    }
}