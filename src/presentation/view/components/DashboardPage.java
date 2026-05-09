package presentation.view.components;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import presentation.controller.MainController;
import presentation.controller.TransactionController;

public class DashboardPage extends VBox {

    public DashboardPage(TransactionController transactionController, MainController mainController, Runnable onDataChanged) {
        super(20);
        this.setPadding(new Insets(20));
        this.setMaxWidth(Double.MAX_VALUE);

        // боксы объявляются ПЕРВЫМИ — они нужны в колбэке transactionTable
        SummaryBox expensesBox = new SummaryBox("Expenses by Category",
                String.format("$%.2f", mainController.getTotalExpense()),
                true, mainController, transactionController);

        SummaryBox incomeBox = new SummaryBox("Income by Category",
                String.format("$%.2f", mainController.getTotalIncome()),
                false, mainController, transactionController);

        TransactionTable transactionTable = new TransactionTable(transactionController, () -> {
            onDataChanged.run();
            expensesBox.refresh();
            incomeBox.refresh();
        });

        TransactionForm formBox = new TransactionForm(transactionController, () -> {
            transactionTable.refresh();
            onDataChanged.run();
            expensesBox.refresh();
            incomeBox.refresh();
        });

        HBox rightBoxes = new HBox(12, expensesBox, incomeBox);
        HBox.setHgrow(expensesBox, Priority.ALWAYS);
        HBox.setHgrow(incomeBox, Priority.ALWAYS);
        expensesBox.setMaxWidth(Double.MAX_VALUE);
        incomeBox.setMaxWidth(Double.MAX_VALUE);
        rightBoxes.setMaxWidth(Double.MAX_VALUE);

        HBox topSection = new HBox(20);
        topSection.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(formBox, Priority.ALWAYS);
        HBox.setHgrow(rightBoxes, Priority.ALWAYS);
        formBox.prefWidthProperty().bind(topSection.widthProperty().multiply(0.55));
        rightBoxes.prefWidthProperty().bind(topSection.widthProperty().multiply(0.44));

        topSection.getChildren().addAll(formBox, rightBoxes);
        this.getChildren().addAll(topSection, transactionTable);
    }
}