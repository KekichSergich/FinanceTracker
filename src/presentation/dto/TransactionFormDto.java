package presentation.dto;

import domain.model.Category;
import domain.model.TransactionType;

import java.time.LocalDate;

public class TransactionFormDto {

    private double amount;
    private TransactionType type;
    private Category category;
    private LocalDate date;
    private String note;

    // getters / setters
}