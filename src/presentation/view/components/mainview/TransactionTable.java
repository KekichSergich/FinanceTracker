package presentation.view.components.mainview;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import presentation.controller.TransactionController;
import presentation.dto.TransactionDto;

/**
 * JavaFX component that displays all transactions in a table.
 * The table also provides row-level deletion and refreshes related UI data after changes.
 */
public class TransactionTable extends VBox {

    private final TableView<TransactionDto> table;
    private final TransactionController transactionController;
    private final Runnable onDataChanged;

    /**
     * Creates a transaction table component.
     *
     * @param transactionController controller used to load and delete transactions
     * @param onDataChanged callback executed after transaction data is changed
     */
    public TransactionTable(TransactionController transactionController, Runnable onDataChanged) {
        super(8);
        this.transactionController = transactionController;
        this.onDataChanged = onDataChanged;

        this.setStyle("""
            -fx-background-color: white;
            -fx-padding: 20;
            -fx-background-radius: 8;
        """);
        this.setMinHeight(400);

        Label tableTitle = new Label("Transactions");
        tableTitle.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #1a1a1a;");

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(400);

        // Define table columns matching fields from TransactionDto.
        TableColumn<TransactionDto, String> dateCol     = new TableColumn<>("Date");
        TableColumn<TransactionDto, String> typeCol     = new TableColumn<>("Type");
        TableColumn<TransactionDto, String> categoryCol = new TableColumn<>("Category");
        TableColumn<TransactionDto, String> amountCol   = new TableColumn<>("Amount");
        TableColumn<TransactionDto, String> noteCol     = new TableColumn<>("Note");

        // Bind table columns to TransactionDto properties.
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        noteCol.setCellValueFactory(new PropertyValueFactory<>("note"));

        // Add an action column with a delete button for each transaction row.
        TableColumn<TransactionDto, Void> deleteCol = new TableColumn<>("");
        deleteCol.setMaxWidth(50);
        deleteCol.setMinWidth(50);

        deleteCol.setCellFactory(col -> new TableCell<>() {
            private final Button deleteBtn = new Button("🗑");

            {
                deleteBtn.setStyle("""
                    -fx-background-color: #ef9a9a;
                    -fx-text-fill: white;
                    -fx-background-radius: 6;
                    -fx-font-size: 14;
                    -fx-cursor: hand;
                    -fx-padding: 6 10 6 10;
                """);

                deleteBtn.setOnAction(e -> {
                    // Get transaction assigned to the current table row.
                    TransactionDto dto = getTableView().getItems().get(getIndex());

                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                            "Delete this transaction?", ButtonType.YES, ButtonType.NO);

                    confirm.showAndWait().ifPresent(bt -> {
                        if (bt == ButtonType.YES) {
                            transactionController.handleDeleteTransaction(dto.getId());

                            // Refresh this table and notify parent components to update summaries/charts.
                            refresh();
                            onDataChanged.run();
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                // Hide the delete button for empty table rows.
                setGraphic(empty ? null : deleteBtn);
                setAlignment(javafx.geometry.Pos.CENTER);
            }
        });

        table.getColumns().addAll(dateCol, typeCol, categoryCol, amountCol, noteCol, deleteCol);
        table.getItems().setAll(transactionController.getAllTransactions());

        this.getChildren().addAll(tableTitle, table);
    }

    /**
     * Reloads transactions from the controller and updates the table content.
     */
    public void refresh() {
        table.getItems().setAll(transactionController.getAllTransactions());
    }
}