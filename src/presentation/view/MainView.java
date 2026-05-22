package presentation.view;

import application.service.DataService;
import application.service.StatisticsService;
import application.service.TransactionService;
import infrastructure.persistence.datasource.DatabaseConnectionFactory;
import infrastructure.persistence.mapper.TransactionMapper;
import infrastructure.persistence.repository.FileTransactionRepository;
import infrastructure.persistence.repository.JdbcTransactionRepository;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import presentation.controller.DataController;
import presentation.controller.MainController;
import presentation.controller.StatisticsController;
import presentation.controller.TransactionController;
import presentation.view.components.mainview.DashboardPage;
import presentation.view.components.common.NavTabs;
import presentation.view.components.common.SummaryCard;
import presentation.view.pages.DataPage;
import presentation.view.pages.StatisticsView;

import java.nio.file.Path;

/**
 * Main JavaFX application entry point.
 * Responsible for dependency initialization,
 * controller wiring, and creation of the main UI layout.
 */
public class MainView extends Application {

    private MainController mainController;
    private TransactionController transactionController;
    private StatisticsController statisticsController;

    /**
     * Initializes and displays the main application window.
     *
     * @param primaryStage primary JavaFX stage
     */
    @Override
    public void start(Stage primaryStage) {

//        // Initialize repositories, services, and controllers.
//

        var factory = new DatabaseConnectionFactory("jdbc:sqlite:data/finance.db");
        var repo = new JdbcTransactionRepository(factory.createConnection(), new TransactionMapper());

        var transactionService = new TransactionService(repo);
        var statisticsService = new StatisticsService(repo);
        var dataService = new DataService(repo);

        transactionController = new TransactionController(transactionService);
        mainController = new MainController(transactionService, statisticsService);
        statisticsController = new StatisticsController(statisticsService);

        var dataController = new DataController(dataService);

        // Main application container.
        VBox container = new VBox(0);
        container.setMaxWidth(1300);
        container.setStyle("-fx-background-color: #f5f5f5;");

        // Application header.
        Label title = new Label("Financial Tracker");
        title.setStyle("""
    -fx-font-size: 22;
    -fx-font-weight: bold;
    -fx-text-fill: #1a1a1a;
    -fx-padding: 20;
""");

// logging toggle
        CheckBox logToggle = new CheckBox("Debug");
        logToggle.setSelected(util.AppLogger.isEnabled());
        logToggle.setStyle("-fx-text-fill: #999; -fx-font-size: 12;");
        logToggle.setOnAction(e -> util.AppLogger.setEnabled(logToggle.isSelected()));

// spacer pushes toggle to the right
        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        HBox header = new HBox();
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        header.getChildren().addAll(title, headerSpacer, logToggle);
        header.setPadding(new Insets(0, 20, 0, 0));

        // Summary cards displaying current financial information.
        SummaryCard balanceCard = new SummaryCard(
                "Total Balance",
                String.format("$%.2f", mainController.getCurrentBalance()),
                "#1a1a1a"
        );

        SummaryCard incomeCard = new SummaryCard(
                "Total Income",
                String.format("$%.2f", mainController.getTotalIncome()),
                "#43a047"
        );

        SummaryCard expenseCard = new SummaryCard(
                "Total Expenses",
                String.format("$%.2f", mainController.getTotalExpense()),
                "#e53935"
        );

        // Updates dashboard card values after data changes.
        Runnable refreshCards = () -> {
            balanceCard.refresh(
                    String.format("$%.2f", mainController.getCurrentBalance())
            );

            incomeCard.refresh(
                    String.format("$%.2f", mainController.getTotalIncome())
            );

            expenseCard.refresh(
                    String.format("$%.2f", mainController.getTotalExpense())
            );
        };

        HBox.setHgrow(balanceCard, Priority.ALWAYS);
        HBox.setHgrow(incomeCard, Priority.ALWAYS);
        HBox.setHgrow(expenseCard, Priority.ALWAYS);

        HBox cards = new HBox(20);
        cards.setPadding(new Insets(0, 20, 20, 20));
        cards.getChildren().addAll(balanceCard, incomeCard, expenseCard);

        // Create dashboard page as default application view.
        DashboardPage dashboardPage =
                new DashboardPage(
                        transactionController,
                        mainController,
                        refreshCards
                );

        // Main content container used for page switching.
        StackPane contentArea = new StackPane();
        contentArea.setAlignment(Pos.TOP_LEFT);
        contentArea.getChildren().setAll(dashboardPage);

        // Refresh dashboard after successful data import.
        Runnable onImport = () -> {
            refreshCards.run();

            contentArea.getChildren().setAll(
                    new DashboardPage(
                            transactionController,
                            mainController,
                            refreshCards
                    )
            );
        };

        // Navigation component used for switching between pages.
        NavTabs navTabs = new NavTabs(
                () -> contentArea.getChildren().setAll(dashboardPage),

                () -> contentArea.getChildren().setAll(
                        new StatisticsView(statisticsController)
                ),

                () -> contentArea.getChildren().setAll(
                        new DataPage(dataController, onImport)
                )
        );

        HBox navWrapper = new HBox();
        navWrapper.setPadding(new Insets(0, 20, 12, 20));
        navWrapper.getChildren().add(navTabs);

        container.getChildren().addAll(header, cards, navWrapper, contentArea);

        // Root application layout.
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");
        root.setCenter(container);

        // Enable scrolling for smaller window sizes.
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("""
            -fx-background: #f5f5f5;
            -fx-background-color: #f5f5f5;
        """);

        Scene scene = new Scene(scrollPane, 1300, 800);

        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.setTitle("Finance Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Application entry point.
     *
     * @param args launch arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}