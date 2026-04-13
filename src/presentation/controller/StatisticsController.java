package presentation.controller;

import application.service.StatisticsService;
import domain.model.Category;

import java.time.YearMonth;
import java.util.Map;

public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    public Map<Category, Double> getExpensesByCategory() {
        // TODO
        return null;
    }

    public Map<YearMonth, Double> getExpensesByMonth() {
        // TODO
        return null;
    }

    public double getTotalIncome() {
        // TODO
        return 0;
    }

    public double getTotalExpense() {
        // TODO
        return 0;
    }
}