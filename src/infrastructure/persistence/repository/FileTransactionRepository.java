package infrastructure.persistence.repository;

import domain.model.Category;
import domain.model.Transaction;
import domain.repository.TransactionRepository;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class FileTransactionRepository implements TransactionRepository {

    private final Path filePath;

    public FileTransactionRepository(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public void save(Transaction transaction) {
        // TODO
    }

    @Override
    public void update(Transaction transaction) {
        // TODO
    }

    @Override
    public void deleteById(Long id) {
        // TODO
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        // TODO
        return Optional.empty();
    }

    @Override
    public List<Transaction> findAll() {
        // TODO
        return List.of();
    }

    @Override
    public List<Transaction> findByCategory(Category category) {
        // TODO
        return List.of();
    }

    @Override
    public List<Transaction> findByDateRange(LocalDate from, LocalDate to) {
        // TODO
        return List.of();
    }
}