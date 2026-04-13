package infrastructure.persistence.mapper;

import domain.model.Transaction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TransactionMapper {

    public Transaction mapRow(ResultSet rs) {
        // TODO
        return null;
    }

    public void mapToStatement(PreparedStatement ps, Transaction transaction) {
        // TODO
    }
}