package domain.model;

import java.time.LocalDate;
import java.util.Objects;

public class Transaction {

    private Long id;
    private double amount;
    private TransactionType type;
    private Category category;
    private LocalDate date;
    private String note;

    public Transaction(Long id, double amount, TransactionType type,
                       Category category, LocalDate date, String note) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.date = date;
        this.note = note;
    }

    public boolean isExpense() { return type == TransactionType.EXPENSE; }
    public boolean isIncome()  { return type == TransactionType.INCOME; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction t)) return false;
        return Objects.equals(id, t.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Transaction{id=%d, amount=%.2f, type=%s, category=%s, date=%s}"
                .formatted(id, amount, type, category, date);
    }
}