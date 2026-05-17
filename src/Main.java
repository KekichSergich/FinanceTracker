import util.AppLogger;

public class Main {
    public static void main(String[] args) {
        // enable logging if --debug flag is passed at startup
        boolean debug = java.util.Arrays.asList(args).contains("--debug");
        AppLogger.configure(debug);

        presentation.view.MainView.main(args);
    }
}