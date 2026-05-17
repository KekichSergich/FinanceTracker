package presentation.view.components.mainview;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import presentation.controller.MainController;
import presentation.controller.TransactionController;

public class DashboardPage extends VBox {

    /**
     * Main dashboard layout combining the transaction form, summary boxes,
     * and transaction table. Accepts a callback to refresh top-level summary cards.
     */
    public DashboardPage(TransactionController transactionController, MainController mainController, Runnable onDataChanged) {
        super(20);
        this.setPadding(new Insets(20));
        this.setMaxWidth(Double.MAX_VALUE);

        // summary boxes must be declared first — they are referenced in table and form callbacks
        SummaryBox expensesBox = new SummaryBox("Expenses by Category",
                String.format("$%.2f", mainController.getTotalExpense()),
                true, mainController, transactionController);

        SummaryBox incomeBox = new SummaryBox("Income by Category",
                String.format("$%.2f", mainController.getTotalIncome()),
                false, mainController, transactionController);

        // on delete: refresh summary cards, expenses box, and income box
        TransactionTable transactionTable = new TransactionTable(transactionController, () -> {
            onDataChanged.run();
            expensesBox.refresh();
            incomeBox.refresh();
        });

        // on add: refresh table, summary cards, expenses box, and income box
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

        // top section: form takes 55%, summary boxes take 44%
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