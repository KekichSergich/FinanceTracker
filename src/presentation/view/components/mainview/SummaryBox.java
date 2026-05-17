package presentation.view.components.mainview;

import domain.model.Category;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import presentation.controller.MainController;
import presentation.controller.TransactionController;

public class SummaryBox extends VBox {

    private final Label valueLabel;
    private final ComboBox<String> filterCombo;
    private final boolean isExpense;
    private final MainController mainController;
    private final TransactionController transactionController;

    /**
     * Creates a summary box showing total expenses or income,
     * with a category filter dropdown.
     *
     * @param isExpense true for expenses box, false for income box
     */
    public SummaryBox(String title, String value, boolean isExpense,
                      MainController mainController,
                      TransactionController transactionController) {
        super(12);
        this.isExpense = isExpense;
        this.mainController = mainController;
        this.transactionController = transactionController;

        this.setStyle("""
            -fx-background-color: white;
            -fx-padding: 20;
            -fx-background-radius: 8;
        """);
        this.setMinHeight(300);
        this.setMaxHeight(300);
        this.setAlignment(Pos.TOP_LEFT);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: #1a1a1a;");

        valueLabel = new Label(value);
        String valueColor = isExpense ? "#e53935" : "#43a047";
        valueLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: " + valueColor + ";");

        // populate filter with all categories plus "All" option
        filterCombo = new ComboBox<>();
        filterCombo.getItems().add("All");
        for (Category c : Category.values()) {
            filterCombo.getItems().add(c.name());
        }
        filterCombo.setValue("All");
        filterCombo.setMaxWidth(Double.MAX_VALUE);
        filterCombo.setStyle("""
            -fx-background-color: #f5f5f5;
            -fx-background-radius: 6;
            -fx-padding: 4;
            -fx-font-size: 12;
        """);

        filterCombo.setOnAction(e -> updateValue());

        this.getChildren().addAll(titleLabel, filterCombo, valueLabel);
    }

    /**
     * Recalculates and updates the displayed value based on selected filter.
     * Shows total for all categories or filtered by specific category.
     */
    private void updateValue() {
        String selected = filterCombo.getValue();
        double total;
        if (selected.equals("All")) {
            total = isExpense
                    ? mainController.getTotalExpense()
                    : mainController.getTotalIncome();
        } else {
            Category cat = Category.valueOf(selected);
            total = transactionController.getAllTransactions().stream()
                    .filter(t -> t.getCategory().equals(cat.name()))
                    .filter(t -> isExpense
                            ? t.getType().equals("EXPENSE")
                            : t.getType().equals("INCOME"))
                    .mapToDouble(t -> Double.parseDouble(t.getAmount()))
                    .sum();
        }
        valueLabel.setText(String.format("$%.2f", total));
    }

    /**
     * Refreshes the displayed value — called after data changes.
     */
    public void refresh() {
        updateValue();
    }
}