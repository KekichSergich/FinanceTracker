package application.service;

import domain.model.Category;
import domain.repository.TransactionRepository;

import java.time.YearMonth;
import java.util.Map;

public class StatisticsService {

    private final TransactionRepository transactionRepository;

    public StatisticsService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public double getTotalIncome() {
        // TODO
        return 0;
    }

    public double getTotalExpense() {
        // TODO
        return 0;
    }

    public double getBalance() {
        // TODO
        return 0;
    }

    public Map<Category, Double> getExpensesByCategory() {
        // TODO
        return null;
    }

    public Map<YearMonth, Double> getExpensesByMonth() {
        // TODO
        return null;
    }
}