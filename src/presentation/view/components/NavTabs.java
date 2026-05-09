package presentation.view.components;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class NavTabs extends HBox {

    public NavTabs(Runnable onDashboard, Runnable onTransactions,
                   Runnable onAnalytics, Runnable onData) {
        super(4);

        Button dashboard    = createTab("Dashboard");
        Button transactions = createTab("Transactions");
        Button analytics    = createTab("Analytics");
        Button data         = createTab("Data");

        // active tab style
        setActive(dashboard);

        dashboard.setOnAction(e -> { setActive(dashboard); onDashboard.run(); });
        transactions.setOnAction(e -> { setActive(transactions); onTransactions.run(); });
        analytics.setOnAction(e -> { setActive(analytics); onAnalytics.run(); });
        data.setOnAction(e -> { setActive(data); onData.run(); });

        this.setPadding(new Insets(8, 20, 8, 20));
        this.setStyle("-fx-background-color: #e8e8e8; -fx-background-radius: 20;");
        this.getChildren().addAll(dashboard, transactions, analytics, data);
    }

    private Button createTab(String title) {
        Button btn = new Button(title);
        btn.setStyle("""
            -fx-background-color: transparent;
            -fx-background-radius: 20;
            -fx-padding: 8 20 8 20;
            -fx-font-size: 13;
            -fx-cursor: hand;
            -fx-text-fill: #666;
        """);
        return btn;
    }

    private void setActive(Button btn) {
        // reset all tabs first
        this.getChildren().forEach(node -> node.setStyle("""
            -fx-background-color: transparent;
            -fx-background-radius: 20;
            -fx-padding: 8 20 8 20;
            -fx-font-size: 13;
            -fx-cursor: hand;
            -fx-text-fill: #666;
        """));
        // highlight active tab
        btn.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 20;
            -fx-padding: 8 20 8 20;
            -fx-font-size: 13;
            -fx-cursor: hand;
            -fx-text-fill: black;
            -fx-effect: dropshadow(gaussian, #ccc, 4, 0, 0, 1);
        """);
    }
}