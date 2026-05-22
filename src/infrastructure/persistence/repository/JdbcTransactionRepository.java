package infrastructure.persistence.repository;

import domain.model.Category;
import domain.model.Transaction;
import domain.repository.TransactionRepository;
import infrastructure.persistence.mapper.TransactionMapper;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC-based implementation of TransactionRepository using SQLite.
 */
public class JdbcTransactionRepository implements TransactionRepository {

    private final Connection connection;
    private final TransactionMapper mapper;

    public JdbcTransactionRepository(Connection connection, TransactionMapper mapper) {
        this.connection = connection;
        this.mapper = mapper;
    }

    /**
     * Inserts a new transaction and assigns the generated ID.
     */
    @Override
    public void save(Transaction transaction) {
        String sql = "INSERT INTO transactions (amount, type, category, date, note) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            mapper.mapToStatement(ps, transaction);
            ps.executeUpdate();
            // read auto-generated id from DB and assign to object
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) transaction.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving transaction", e);
        }
    }

    /**
     * Updates an existing transaction by ID.
     */
    @Override
    public void update(Transaction transaction) {
        String sql = "UPDATE transactions SET amount=?, type=?, category=?, date=?, note=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            mapper.mapToStatement(ps, transaction);
            ps.setLong(6, transaction.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating transaction", e);
        }
    }

    /**
     * Deletes a transaction by ID.
     */
    @Override
    public void deleteById(Long id) {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM transactions WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting transaction", e);
        }
    }

    /**
     * Finds a transaction by ID.
     */
    @Override
    public Optional<Transaction> findById(Long id) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM transactions WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Transaction transaction = mapper.mapRow(rs);
                    return Optional.of(transaction);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding transaction", e);
        }
        return Optional.empty();
    }

    /**
     * Returns all transactions ordered by date descending.
     */
    @Override
    public List<Transaction> findAll() {
        List<Transaction> list = new ArrayList<>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT * FROM transactions ORDER BY date DESC")) {
            while (rs.next()) list.add(mapper.mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching transactions", e);
        }
        return list;
    }

    /**
     * Returns transactions filtered by category.
     */
    @Override
    public List<Transaction> findByCategory(Category category) {
        List<Transaction> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM transactions WHERE category=? ORDER BY date DESC")) {
            ps.setString(1, category.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error filtering by category", e);
        }
        return list;
    }

    /**
     * Returns transactions within a date range.
     */
    @Override
    public List<Transaction> findByDateRange(LocalDate from, LocalDate to) {
        List<Transaction> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM transactions WHERE date >= ? AND date <= ? ORDER BY date DESC")) {
            ps.setString(1, from.toString());
            ps.setString(2, to.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error filtering by date range", e);
        }
        return list;
    }

    /**
     * Deletes all transactions from the database.
     */
    @Override
    public void deleteAll() {
        try (Statement st = connection.createStatement()) {
            st.execute("DELETE FROM transactions");
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting all transactions", e);
        }
    }

    /**
     * Saves multiple transactions at once.
     */
    @Override
    public void saveAll(List<Transaction> transactions) {
        for (Transaction t : transactions) {
            save(t);
        }
    }
}