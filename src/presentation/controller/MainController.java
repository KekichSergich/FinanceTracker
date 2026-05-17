package presentation.controller;

import application.service.StatisticsService;
import application.service.TransactionService;
import domain.model.Category;

import java.util.Map;

public class MainController {

    private final TransactionService transactionService;
    private final StatisticsService statisticsService;

    public MainController(TransactionService transactionService,
                          StatisticsService statisticsService) {
        this.transactionService = transactionService;
        this.statisticsService = statisticsService;
    }

    /**
     * Returns current balance (total income minus total expenses).
     */
    public double getCurrentBalance() {
        return statisticsService.getBalance();
    }

    /**
     * Returns the sum of all income transactions.
     */
    public double getTotalIncome() {
        return statisticsService.getTotalIncome();
    }

    /**
     * Returns the sum of all expense transactions.
     */
    public double getTotalExpense() {
        return statisticsService.getTotalExpense();
    }

    /**
     * Returns a map of expense amounts grouped by category.
     */
    public Map<Category, Double> getExpensesByCategory() {
        return statisticsService.getExpensesByCategory();
    }
}