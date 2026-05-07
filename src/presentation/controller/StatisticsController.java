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
        return statisticsService.getExpensesByCategory();
    }

    public Map<YearMonth, Double> getExpensesByMonth() {
        return  statisticsService.getExpensesByMonth();
    }

    public double getTotalIncome() {
        return statisticsService.getTotalIncome();
    }

    public double getTotalExpense() {
        return statisticsService.getTotalExpense();
    }
}