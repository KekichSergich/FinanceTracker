package application.service;

import domain.model.Category;
import domain.model.Transaction;
import domain.model.TransactionType;
import domain.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link TransactionService}.
 * Uses an in-memory repository to avoid file system dependency.
 */
class TransactionServiceTest {

    private List<Transaction> storage;
    private TransactionRepository repo;
    private TransactionService service;

    /**
     * Sets up a fresh in-memory repository and service before each test.
     */
    @BeforeEach
    void setUp() {
        storage = new ArrayList<>();
        repo = new TransactionRepository() {
            long nextId = 1;

            @Override public void save(Transaction t) { t.setId(nextId++); storage.add(t); }
            @Override public void update(Transaction t) {}
            @Override public void deleteById(Long id) { storage.removeIf(t -> t.getId().equals(id)); }
            @Override public Optional<Transaction> findById(Long id) { return storage.stream().filter(t -> t.getId().equals(id)).findFirst(); }
            @Override public List<Transaction> findAll() { return new ArrayList<>(storage); }
            @Override public List<Transaction> findByCategory(Category c) { return List.of(); }
            @Override public List<Transaction> findByDateRange(java.time.LocalDate f, java.time.LocalDate t) { return List.of(); }
            @Override public void deleteAll() { storage.clear(); }
            @Override public void saveAll(List<Transaction> list) { list.forEach(t -> { t.setId(nextId++); storage.add(t); }); }
        };
        service = new TransactionService(repo);
    }

    /**
     * Verifies that a transaction with a valid positive amount is saved successfully.
     */
    @Test
    void addTransaction_validAmount_savesTransaction() {
        Transaction t = new Transaction(null, 100.0, TransactionType.INCOME, Category.FOOD, LocalDate.now(), "test");
        service.addTransaction(t);
        assertEquals(1, service.getAllTransactions().size());
    }

    /**
     * Verifies that adding a transaction with zero amount throws IllegalArgumentException.
     */
    @Test
    void addTransaction_zeroAmount_throwsException() {
        Transaction t = new Transaction(null, 0.0, TransactionType.EXPENSE, Category.FOOD, LocalDate.now(), "test");
        assertThrows(IllegalArgumentException.class, () -> service.addTransaction(t));
    }

    /**
     * Verifies that adding a transaction with a negative amount throws IllegalArgumentException.
     */
    @Test
    void addTransaction_negativeAmount_throwsException() {
        Transaction t = new Transaction(null, -50.0, TransactionType.EXPENSE, Category.FOOD, LocalDate.now(), "test");
        assertThrows(IllegalArgumentException.class, () -> service.addTransaction(t));
    }

    /**
     * Verifies that deleting a transaction by ID removes it from storage.
     */
    @Test
    void deleteTransaction_existingId_removesTransaction() {
        Transaction t = new Transaction(null, 100.0, TransactionType.INCOME, Category.FOOD, LocalDate.now(), "test");
        service.addTransaction(t);
        service.deleteTransaction(t.getId());
        assertEquals(0, service.getAllTransactions().size());
    }

    /**
     * Verifies that getAllTransactions returns all previously saved transactions.
     */
    @Test
    void getAllTransactions_returnsAllSaved() {
        service.addTransaction(new Transaction(null, 100.0, TransactionType.INCOME, Category.FOOD, LocalDate.now(), "a"));
        service.addTransaction(new Transaction(null, 200.0, TransactionType.EXPENSE, Category.FOOD, LocalDate.now(), "b"));
        assertEquals(2, service.getAllTransactions().size());
    }
}