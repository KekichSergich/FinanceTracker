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
        return statisticsService.getBalance();
    }

    public double getTotalIncome() {
        return statisticsService.getTotalIncome();
    }

    public double getTotalExpense() {
        return statisticsService.getTotalExpense();
    }

    public Map<Category, Double> getExpensesByCategory() {
        return  statisticsService.getExpensesByCategory();
    }
}