package infrastructure.persistence.mapper;

import domain.model.Category;
import domain.model.Transaction;
import domain.model.TransactionType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Maps between JDBC ResultSet and Transaction domain object.
 */
public class TransactionMapper {

    /**
     * Maps a single ResultSet row to a Transaction object.
     *
     * @param rs result set positioned at the current row
     * @return mapped Transaction
     */
    public Transaction mapRow(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        double amount = rs.getDouble("amount");
        TransactionType type = TransactionType.valueOf(rs.getString("type"));
        Category category = Category.valueOf(rs.getString("category"));
        LocalDate date = LocalDate.parse(rs.getString("date"));
        String note = rs.getString("note");
        return new Transaction(id, amount, type, category, date, note);
    }

    /**
     * Maps a Transaction to a PreparedStatement for INSERT or UPDATE.
     * Parameter order: amount, type, category, date, note.
     *
     * @param ps          prepared statement to fill
     * @param transaction transaction to map
     */
    public void mapToStatement(PreparedStatement ps, Transaction transaction) throws SQLException {
        ps.setDouble(1, transaction.getAmount());
        ps.setString(2, transaction.getType().name());
        ps.setString(3, transaction.getCategory().name());
        ps.setString(4, transaction.getDate().toString());
        ps.setString(5, transaction.getNote() != null ? transaction.getNote() : "");
    }
}