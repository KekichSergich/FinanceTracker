package application.service;

import domain.model.Category;
import domain.model.Transaction;
import domain.repository.TransactionRepository;

import java.time.YearMonth;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticsService {

    private final TransactionRepository transactionRepository;

    public StatisticsService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public double getTotalIncome() {
        return transactionRepository.findAll().stream()
                .filter(Transaction::isIncome)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalExpense() {
        return transactionRepository.findAll().stream()
                .filter(Transaction::isExpense)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getBalance() {
        return getTotalIncome() - getTotalExpense();
    }

    public Map<Category, Double> getExpensesByCategory() {
        return transactionRepository.findAll().stream()
                .filter(Transaction::isExpense)
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    public Map<YearMonth, Double> getExpensesByMonth() {
        return transactionRepository.findAll().stream()
                .filter(Transaction::isExpense)
                .collect(Collectors.groupingBy(
                        t -> YearMonth.from(t.getDate()),
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }
}