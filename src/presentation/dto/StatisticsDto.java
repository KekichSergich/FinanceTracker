package presentation.dto;

import java.util.Map;

public class StatisticsDto {

    private double totalIncome;
    private double totalExpense;
    private double balance;

    private Map<String, Double> expensesByCategory;
    private Map<String, Double> expensesByMonth;

    // getters / setters
}