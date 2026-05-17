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

    /**
     * Returns expense amounts grouped by category.
     */
    public Map<Category, Double> getExpensesByCategory() {
        return statisticsService.getExpensesByCategory();
    }

    /**
     * Returns expense amounts grouped by month.
     */
    public Map<YearMonth, Double> getExpensesByMonth() {
        return statisticsService.getExpensesByMonth();
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
     * Returns income amounts grouped by category.
     */
    public Map<Category, Double> getIncomeByCategory() {
        return statisticsService.getIncomeByCategory();
    }

    /**
     * Returns cumulative balance grouped by month, sorted chronologically.
     */
    public Map<YearMonth, Double> getBalanceByMonth() {
        return statisticsService.getBalanceByMonth();
    }
}