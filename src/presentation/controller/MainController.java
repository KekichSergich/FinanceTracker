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

    public double getCurrentBalance() {
        // TODO
        return 0;
    }

    public double getTotalIncome() {
        // TODO
        return 0;
    }

    public double getTotalExpense() {
        // TODO
        return 0;
    }

    public Map<Category, Double> getExpensesByCategory() {
        // TODO
        return null;
    }
}