package application.service;

import domain.model.Category;
import domain.model.Transaction;
import domain.repository.TransactionRepository;
import util.AppLogger;

import java.time.YearMonth;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class StatisticsService {

    private static final Logger logger = AppLogger.get(StatisticsService.class);

    private final TransactionRepository transactionRepository;

    public StatisticsService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Returns the sum of all income transactions.
     */
    public double getTotalIncome() {
        double result = transactionRepository.findAll().stream()
                .filter(Transaction::isIncome)
                .mapToDouble(Transaction::getAmount)
                .sum();
        logger.fine("Total income calculated: " + result);
        return result;
    }

    /**
     * Returns the sum of all expense transactions.
     */
    public double getTotalExpense() {
        double result = transactionRepository.findAll().stream()
                .filter(Transaction::isExpense)
                .mapToDouble(Transaction::getAmount)
                .sum();
        logger.fine("Total expense calculated: " + result);
        return result;
    }

    /**
     * Returns current balance (total income minus total expenses).
     */
    public double getBalance() {
        double result = getTotalIncome() - getTotalExpense();
        logger.fine("Balance calculated: " + result);
        return result;
    }

    /**
     * Returns expense amounts grouped by category.
     */
    public Map<Category, Double> getExpensesByCategory() {
        Map<Category, Double> result = transactionRepository.findAll().stream()
                .filter(Transaction::isExpense)
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
        logger.fine("Expenses by category calculated: " + result.size() + " categories");
        return result;
    }

    /**
     * Returns expense amounts grouped by month.
     */
    public Map<YearMonth, Double> getExpensesByMonth() {
        Map<YearMonth, Double> result = transactionRepository.findAll().stream()
                .filter(Transaction::isExpense)
                .collect(Collectors.groupingBy(
                        t -> YearMonth.from(t.getDate()),
                        Collectors.summingDouble(Transaction::getAmount)
                ));
        logger.fine("Expenses by month calculated: " + result.size() + " months");
        return result;
    }

    /**
     * Returns income amounts grouped by category.
     */
    public Map<Category, Double> getIncomeByCategory() {
        Map<Category, Double> result = transactionRepository.findAll().stream()
                .filter(Transaction::isIncome)
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
        logger.fine("Income by category calculated: " + result.size() + " categories");
        return result;
    }

    /**
     * Returns cumulative balance grouped by month, sorted chronologically.
     * Each month's value includes all previous months.
     */
    public Map<YearMonth, Double> getBalanceByMonth() {
        Map<YearMonth, Double> income = transactionRepository.findAll().stream()
                .filter(Transaction::isIncome)
                .collect(Collectors.groupingBy(
                        t -> YearMonth.from(t.getDate()),
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        Map<YearMonth, Double> expense = transactionRepository.findAll().stream()
                .filter(Transaction::isExpense)
                .collect(Collectors.groupingBy(
                        t -> YearMonth.from(t.getDate()),
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        // collect all months and sort them
        TreeMap<YearMonth, Double> balance = new TreeMap<>();
        Set<YearMonth> allMonths = new HashSet<>();
        allMonths.addAll(income.keySet());
        allMonths.addAll(expense.keySet());

        // cumulative balance — each month adds to previous
        double cumulative = 0.0;
        for (YearMonth month : new TreeSet<>(allMonths)) {
            double inc = income.getOrDefault(month, 0.0);
            double exp = expense.getOrDefault(month, 0.0);
            cumulative += inc - exp;
            balance.put(month, cumulative);
        }

        logger.fine("Balance by month calculated: " + balance.size() + " months");
        return balance;
    }
}