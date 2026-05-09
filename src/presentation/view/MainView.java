package presentation.view;

import application.service.StatisticsService;
import application.service.TransactionService;
import infrastructure.persistence.repository.FileTransactionRepository;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import presentation.controller.MainController;
import presentation.controller.StatisticsController;
import presentation.controller.TransactionController;
import presentation.view.components.DashboardPage;
import presentation.view.components.NavTabs;
import presentation.view.components.SummaryCard;

import java.nio.file.Path;

public class MainView extends Application {

    private MainController mainController;
    private TransactionController transactionController;
    private StatisticsController statisticsController;

    @Override
    public void start(Stage primaryStage) {
        // wiring
        var repo = new FileTransactionRepository(Path.of("data/transactions.json"));
        var transactionService = new TransactionService(repo);
        var statisticsService = new StatisticsService(repo);
        transactionController = new TransactionController(transactionService);
        mainController = new MainController(transactionService, statisticsService);
        statisticsController = new StatisticsController(statisticsService);

        // inner container — like max-width: 1300px; margin: 0 auto in CSS
        VBox container = new VBox(0);
        container.setMaxWidth(1300);
        container.setStyle("-fx-background-color: #f5f5f5;");

        // header
        Label title = new Label("Financial Tracker");
        title.setStyle("""
            -fx-font-size: 22;
            -fx-font-weight: bold;
            -fx-text-fill: #1a1a1a;
            -fx-padding: 20;
        """);

        // summary cards
        SummaryCard balanceCard = new SummaryCard("Total Balance",
                String.format("$%.2f", mainController.getCurrentBalance()), "#1a1a1a");
        SummaryCard incomeCard  = new SummaryCard("Total Income",
                String.format("$%.2f", mainController.getTotalIncome()), "#43a047");
        SummaryCard expenseCard = new SummaryCard("Total Expenses",
                String.format("$%.2f", mainController.getTotalExpense()), "#e53935");

        Runnable refreshCards = () -> {
            balanceCard.refresh(String.format("$%.2f", mainController.getCurrentBalance()));
            incomeCard.refresh(String.format("$%.2f", mainController.getTotalIncome()));
            expenseCard.refresh(String.format("$%.2f", mainController.getTotalExpense()));
        };


        HBox.setHgrow(balanceCard, Priority.ALWAYS);
        HBox.setHgrow(incomeCard,  Priority.ALWAYS);
        HBox.setHgrow(expenseCard, Priority.ALWAYS);

        HBox cards = new HBox(20);
        cards.setPadding(new Insets(0, 20, 20, 20));
        cards.getChildren().addAll(balanceCard, incomeCard, expenseCard);

        // navigation tabs
        DashboardPage dashboardPage = new DashboardPage(transactionController, mainController, refreshCards);

        StackPane contentArea = new StackPane();
        contentArea.setAlignment(Pos.TOP_LEFT);
        contentArea.getChildren().setAll(dashboardPage);

        NavTabs navTabs = new NavTabs(
                () -> contentArea.getChildren().setAll(dashboardPage),
                () -> contentArea.getChildren().setAll(new Label("Transactions — coming soon")),
                () -> contentArea.getChildren().setAll(new Label("Analytics — coming soon")),
                () -> contentArea.getChildren().setAll(new Label("Data — coming soon"))
        );

        HBox navWrapper = new HBox();
        navWrapper.setPadding(new Insets(0, 20, 12, 20));
        navWrapper.getChildren().add(navTabs);

        container.getChildren().addAll(title, cards, navWrapper, contentArea);

        // outer wrapper — centers the container like margin: 0 auto
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");
        root.setCenter(container);

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f5f5f5; -fx-background-color: #f5f5f5;");

        Scene scene = new Scene(scrollPane, 1300, 800);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.setTitle("Finance Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}