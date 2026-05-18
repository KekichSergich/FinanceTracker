package application.service;

import domain.model.Category;
import domain.model.ImportMode;
import domain.model.Transaction;
import domain.model.TransactionType;
import domain.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link DataService}.
 * Uses an in-memory repository and JUnit's {@link TempDir} for temporary files.
 */
class DataServiceTest {

    /** Temporary directory created and cleaned up automatically by JUnit. */
    @TempDir
    Path tempDir;

    private List<Transaction> storage;
    private TransactionRepository repo;
    private DataService dataService;

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
            @Override public List<Transaction> findByDateRange(LocalDate f, LocalDate t) { return List.of(); }
            @Override public void deleteAll() { storage.clear(); }
            @Override public void saveAll(List<Transaction> list) { list.forEach(t -> { t.setId(nextId++); storage.add(t); }); }
        };
        dataService = new DataService(repo);
    }

    /**
     * Verifies that exportToJson creates a file containing the transaction data.
     */
    @Test
    void exportToJson_createsFile() throws IOException {
        Transaction t = new Transaction(null, 100.0, TransactionType.INCOME, Category.FOOD, LocalDate.of(2026, 1, 1), "note");
        repo.save(t);

        Path file = tempDir.resolve("export.json");
        dataService.exportToJson(file.toString());

        assertTrue(Files.exists(file));
        String content = Files.readString(file);
        assertTrue(content.contains("\"amount\":100.0"));
        assertTrue(content.contains("\"type\":\"INCOME\""));
    }

    /**
     * Verifies that exportToJson produces a valid empty JSON array when storage is empty.
     */
    @Test
    void exportToJson_emptyStorage_createsEmptyArray() throws IOException {
        Path file = tempDir.resolve("export.json");
        dataService.exportToJson(file.toString());

        String content = Files.readString(file);
        assertTrue(content.startsWith("[") && content.endsWith("]"));
    }

    /**
     * Verifies that importing in REPLACE mode clears existing data and loads only new transactions.
     */
    @Test
    void importFromJson_replaceMode_deletesExisting() throws IOException {
        storage.add(new Transaction(1L, 500.0, TransactionType.EXPENSE, Category.FOOD, LocalDate.of(2026, 1, 1), "old"));

        Path file = tempDir.resolve("import.json");
        Files.writeString(file, """
            [
              {"id":10,"amount":200.0,"type":"INCOME","category":"FOOD","date":"2026-03-01","note":"new"}
            ]
        """);

        dataService.importFromJson(file.toString(), ImportMode.REPLACE);

        assertEquals(1, storage.size());
        assertEquals(200.0, storage.get(0).getAmount());
    }

    /**
     * Verifies that importing in MERGE mode appends new transactions to existing ones.
     */
    @Test
    void importFromJson_mergeMode_appendsToExisting() throws IOException {
        storage.add(new Transaction(1L, 500.0, TransactionType.EXPENSE, Category.FOOD, LocalDate.of(2026, 1, 1), "old"));

        Path file = tempDir.resolve("import.json");
        Files.writeString(file, """
            [
              {"id":10,"amount":200.0,"type":"INCOME","category":"FOOD","date":"2026-03-01","note":"new"}
            ]
        """);

        dataService.importFromJson(file.toString(), ImportMode.MERGE);

        assertEquals(2, storage.size());
    }

    /**
     * Verifies that invalid records are skipped during import and returned as error descriptions.
     */
    @Test
    void importFromJson_invalidRecord_skipsAndReturnsError() throws IOException {
        Path file = tempDir.resolve("import.json");
        Files.writeString(file, """
            [
              {"id":1,"amount":100.0,"type":"INCOME","category":"FOOD","date":"2026-01-01","note":"ok"},
              {"id":2,"amount":"INVALID","type":"INCOME","category":"FOOD","date":"2026-01-01","note":"bad"}
            ]
        """);

        List<String> skipped = dataService.importFromJson(file.toString(), ImportMode.REPLACE);

        assertEquals(1, storage.size());
        assertEquals(1, skipped.size());
    }
}