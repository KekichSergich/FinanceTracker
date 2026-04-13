package domain.repository;

import domain.model.Category;
import domain.model.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

    void save(Transaction transaction);

    void update(Transaction transaction);

    void deleteById(Long id);

    Optional<Transaction> findById(Long id);

    List<Transaction> findAll();

    List<Transaction> findByCategory(Category category);

    List<Transaction> findByDateRange(LocalDate from, LocalDate to);
}
