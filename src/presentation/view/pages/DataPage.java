package presentation.view.pages;

import domain.model.ImportMode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import presentation.controller.DataController;

import java.io.File;
import java.util.List;

public class DataPage extends VBox {

    public DataPage(DataController dataController, Runnable onImport) {
        super(20);
        this.setPadding(new Insets(20));
        this.setMaxWidth(Double.MAX_VALUE);
        this.setMinHeight(600); // ← новая строка

        VBox card = new VBox(12);
        card.setStyle("""
            -fx-background-color: white;
            -fx-padding: 24;
            -fx-background-radius: 8;
        """);

        Label title = new Label("Data Management");
        title.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #1a1a1a;");

        Label subtitle = new Label("Export your financial data as JSON file or import previously saved data.");
        subtitle.setStyle("-fx-font-size: 12; -fx-text-fill: #888;");

        Button exportBtn = new Button("⬇  Export to JSON");
        exportBtn.setStyle("""
            -fx-background-color: #1a1a1a;
            -fx-text-fill: white;
            -fx-font-size: 13;
            -fx-padding: 10 24 10 24;
            -fx-background-radius: 6;
            -fx-cursor: hand;
        """);

        Button importBtn = new Button("⬆  Import from JSON");
        importBtn.setStyle("""
            -fx-background-color: white;
            -fx-text-fill: #1a1a1a;
            -fx-font-size: 13;
            -fx-padding: 10 24 10 24;
            -fx-background-radius: 6;
            -fx-border-color: #ccc;
            -fx-border-radius: 6;
            -fx-cursor: hand;
        """);

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #888;");
        statusLabel.setWrapText(true);

        HBox buttons = new HBox(12, exportBtn, importBtn);
        buttons.setAlignment(Pos.CENTER_LEFT);

        exportBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Save JSON");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
            fc.setInitialFileName("transactions.json");
            File file = fc.showSaveDialog(getScene().getWindow());
            if (file != null) {
                try {
                    dataController.exportToJson(file.getAbsolutePath());
                    statusLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #43a047;");
                    statusLabel.setText("Exported successfully to: " + file.getName());
                } catch (Exception ex) {
                    statusLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #e53935;");
                    statusLabel.setText("Export failed: " + ex.getMessage());
                }
            }
        });

        importBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Open JSON");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
            File file = fc.showOpenDialog(getScene().getWindow());
            if (file == null) return;

            Alert modeDialog = new Alert(Alert.AlertType.CONFIRMATION);
            modeDialog.setTitle("Import Mode");
            modeDialog.setHeaderText("How to import?");
            modeDialog.setContentText("Replace — delete all existing transactions.\nMerge — add imported to existing.");
            ButtonType replaceBtn = new ButtonType("Replace");
            ButtonType mergeBtn   = new ButtonType("Merge");
            ButtonType cancelBtn  = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            modeDialog.getButtonTypes().setAll(replaceBtn, mergeBtn, cancelBtn);

            modeDialog.showAndWait().ifPresent(result -> {
                if (result == cancelBtn) return;
                ImportMode mode = result == replaceBtn ? ImportMode.REPLACE : ImportMode.MERGE;
                try {
                    List<String> skipped = dataController.importFromJson(file.getAbsolutePath(), mode);
                    onImport.run();
                    if (skipped.isEmpty()) {
                        statusLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #43a047;");
                        statusLabel.setText("Import successful.");
                    } else {
                        statusLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #e53935;");
                        statusLabel.setText("Skipped " + skipped.size() + " records:\n" + String.join("\n", skipped));
                    }
                } catch (Exception ex) {
                    statusLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #e53935;");
                    statusLabel.setText("Import failed: " + ex.getMessage());
                }
            });
        });

        card.getChildren().addAll(title, subtitle, buttons, statusLabel);
        this.getChildren().add(card);
    }
}