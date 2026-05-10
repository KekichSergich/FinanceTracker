package presentation.view.components.mainview;

import domain.model.Category;
import domain.model.TransactionType;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import presentation.controller.TransactionController;
import presentation.dto.TransactionFormDto;

import java.time.LocalDate;

public class TransactionForm extends VBox {

    private final TransactionController transactionController;
    private final Runnable onTransactionAdded;

    public TransactionForm(TransactionController transactionController, Runnable onTransactionAdded) {
        super(12);
        this.transactionController = transactionController;
        this.onTransactionAdded = onTransactionAdded;

        String inputStyle = """
            -fx-background-color: #f5f5f5;
            -fx-background-radius: 6;
            -fx-padding: 10;
            -fx-font-size: 13;
        """;
        String labelStyle = "-fx-text-fill: #1a1a1a; -fx-font-weight: bold; -fx-font-size: 13;";

        this.setStyle("""
            -fx-background-color: white;
            -fx-padding: 20;
            -fx-background-radius: 8;
        """);

        Label formTitle = new Label("Add Transaction");
        formTitle.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #1a1a1a;");

        Label typeLabel = new Label("Type");
        typeLabel.setStyle(labelStyle);
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Expense", "Income");
        typeCombo.setValue("Expense");
        typeCombo.setMaxWidth(Double.MAX_VALUE);
        typeCombo.setStyle(inputStyle);

        Label amountLabel = new Label("Amount");
        amountLabel.setStyle(labelStyle);
        TextField amountField = new TextField();
        amountField.setPromptText("0.00");
        amountField.setMaxWidth(Double.MAX_VALUE);
        amountField.setStyle(inputStyle);

        VBox typeBox = new VBox(4, typeLabel, typeCombo);
        VBox amountBox = new VBox(4, amountLabel, amountField);
        HBox typeAndAmount = new HBox(12, typeBox, amountBox);
        HBox.setHgrow(typeBox, Priority.ALWAYS);
        HBox.setHgrow(amountBox, Priority.ALWAYS);
        typeBox.prefWidthProperty().bind(typeAndAmount.widthProperty().multiply(0.5));
        amountBox.prefWidthProperty().bind(typeAndAmount.widthProperty().multiply(0.5));

        Label categoryLabel = new Label("Category");
        categoryLabel.setStyle(labelStyle);
        ComboBox<String> categoryCombo = new ComboBox<>();
        for (Category c : Category.values()) {
            categoryCombo.getItems().add(c.name());
        }
        categoryCombo.setPromptText("Select a category");
        categoryCombo.setMaxWidth(Double.MAX_VALUE);
        categoryCombo.setStyle(inputStyle);

        Label descLabel = new Label("Description");
        descLabel.setStyle(labelStyle);
        TextField descField = new TextField();
        descField.setPromptText("Enter description");
        descField.setMaxWidth(Double.MAX_VALUE);
        descField.setStyle(inputStyle);

        Label dateLabel = new Label("Date");
        dateLabel.setStyle(labelStyle);
        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setMaxWidth(Double.MAX_VALUE);
        datePicker.setStyle(inputStyle);

        Button addBtn = new Button("+ Add Transaction");
        addBtn.setMaxWidth(Double.MAX_VALUE);
        addBtn.setStyle("""
            -fx-background-color: black;
            -fx-text-fill: white;
            -fx-font-size: 13;
            -fx-font-weight: bold;
            -fx-padding: 12;
            -fx-background-radius: 6;
            -fx-cursor: hand;
        """);

        // onClick — save transaction and notify parent via callback
        addBtn.setOnAction(e -> {
            try {
                TransactionFormDto dto = new TransactionFormDto();
                dto.setAmount(Double.parseDouble(amountField.getText()));
                dto.setType(TransactionType.valueOf(typeCombo.getValue().toUpperCase()));
                dto.setCategory(Category.valueOf(categoryCombo.getValue()));
                dto.setNote(descField.getText());
                dto.setDate(datePicker.getValue());

                transactionController.handleAddTransaction(dto);

                // notify DashboardPage to refresh table — like props.onSave() in React
                onTransactionAdded.run();

                amountField.clear();
                descField.clear();
                categoryCombo.setValue(null);
                datePicker.setValue(LocalDate.now());

            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
                alert.showAndWait();
            }
        });

        this.getChildren().addAll(
                formTitle,
                typeAndAmount,
                categoryLabel, categoryCombo,
                descLabel, descField,
                dateLabel, datePicker,
                addBtn
        );
    }
}