package infrastructure.persistence.repository;

import domain.model.Category;
import domain.model.Transaction;
import domain.repository.TransactionRepository;
import infrastructure.persistence.mapper.TransactionMapper;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class JdbcTransactionRepository implements TransactionRepository {

    private final Connection connection;
    private final TransactionMapper mapper;

    public JdbcTransactionRepository(Connection connection,
                                     TransactionMapper mapper) {
        this.connection = connection;
        this.mapper = mapper;
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
