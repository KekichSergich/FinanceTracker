package presentation.view.components.common;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SummaryCard extends VBox {

    private final Label valueLabel;

    /**
     * Creates a summary card with a title, value, and value color.
     */
    public SummaryCard(String title, String value, String valueColor) {

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 13;");

        valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: " + valueColor + ";");

        this.getChildren().addAll(titleLabel, valueLabel);
        this.setSpacing(8);
        this.setAlignment(Pos.CENTER_LEFT);
        this.setStyle("""
            -fx-background-color: white;
            -fx-padding: 20;
            -fx-background-radius: 8;
            -fx-effect: dropshadow(gaussian, #ccc, 6, 0, 0, 2);
        """);
    }

    /**
     * Updates the displayed value — called after data changes.
     */
    public void refresh(String newValue) {
        valueLabel.setText(newValue);
    }
}