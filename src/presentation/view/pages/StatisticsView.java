package presentation.view.pages;

import domain.model.Category;
import javafx.geometry.Insets;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import presentation.controller.StatisticsController;

import java.time.YearMonth;
import java.util.Map;

public class StatisticsView extends VBox {

    public StatisticsView(StatisticsController statisticsController) {
        super(20);
        this.setPadding(new Insets(20));
        this.setMaxWidth(Double.MAX_VALUE);
        this.setMinHeight(800);

        // title
        Label title = new Label("Analytics");
        title.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #1a1a1a;");

        // two pie charts side by side
        PieChart expensePie = buildPieChart(
                "Expenses by Category",
                statisticsController.getExpensesByCategory()
        );

        PieChart incomePie = buildPieChart(
                "Income by Category",
                statisticsController.getIncomeByCategory()
        );

        HBox pieRow = new HBox(20, expensePie, incomePie);
        HBox.setHgrow(expensePie, Priority.ALWAYS);
        HBox.setHgrow(incomePie,  Priority.ALWAYS);
        expensePie.setMaxWidth(Double.MAX_VALUE);
        incomePie.setMaxWidth(Double.MAX_VALUE);
        pieRow.setMaxWidth(Double.MAX_VALUE);

        // cumulative balance line chart below pie charts
        LineChart<String, Number> balanceChart = buildBalanceChart(
                statisticsController.getBalanceByMonth()
        );

        this.getChildren().addAll(title, pieRow, balanceChart);
    }

    /**
     * Builds a pie chart for the given category -> amount data.
     * Shows "No data" slice if the map is empty.
     */
    private PieChart buildPieChart(String title, Map<Category, Double> data) {
        PieChart chart = new PieChart();
        chart.setTitle(title);
        chart.setStyle("-fx-background-color: white; -fx-background-radius: 8;");
        chart.setMinHeight(300);

        if (data.isEmpty()) {
            chart.getData().add(new PieChart.Data("No data", 1));
        } else {
            data.forEach((category, amount) ->
                    chart.getData().add(new PieChart.Data(category.name(), amount))
            );
        }

        return chart;
    }

    /**
     * Builds a line chart showing cumulative balance per month.
     * Data is sorted chronologically before rendering.
     */
    private LineChart<String, Number> buildBalanceChart(Map<YearMonth, Double> data) {
        LineChart<String, Number> chart = getStringNumberLineChart();

        //line balance
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Balance");

        // sort by month and add data points
        data.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry ->
                        series.getData().add(
                                new XYChart.Data<>(entry.getKey().toString(), entry.getValue())
                        )
                );

        // fallback if no transactions exist
        if (data.isEmpty()) {
            series.getData().add(new XYChart.Data<>("No data", 0));
        }

        chart.getData().add(series);
        return chart;
    }

    private static LineChart<String, Number> getStringNumberLineChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Balance ($)");

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Balance by Month");
        chart.setStyle("-fx-background-color: white; -fx-background-radius: 8;");
        chart.setMinHeight(300);
        chart.setMaxWidth(Double.MAX_VALUE);
        //points on chart are turn on
        chart.setCreateSymbols(true);
        return chart;
    }
}