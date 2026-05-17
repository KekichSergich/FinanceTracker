package util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AppLogger {

    private static boolean enabled = false;

    // packages to log — only our code, not JavaFX internals
    private static final String[] APP_PACKAGES = {
            "application",
            "infrastructure",
            "presentation",
            "domain"
    };

    /**
     * Configures logging for application packages only.
     * Called once at startup based on launch arguments or UI toggle.
     */
    public static void configure(boolean enable) {
        enabled = enable;

        // disable root logger to suppress JavaFX internal logs
        Logger root = Logger.getLogger("");
        root.setLevel(Level.OFF);
        for (var handler : root.getHandlers()) {
            root.removeHandler(handler);
        }

        if (enable) {
            ConsoleHandler handler = new ConsoleHandler();
            handler.setLevel(Level.INFO);
            handler.setFormatter(new SimpleFormatter());

            // enable only our packages
            for (String pkg : APP_PACKAGES) {
                Logger pkgLogger = Logger.getLogger(pkg);
                pkgLogger.setLevel(Level.INFO);
                pkgLogger.addHandler(handler);
                pkgLogger.setUseParentHandlers(false);
            }
        } else {
            // disable our packages too
            for (String pkg : APP_PACKAGES) {
                Logger pkgLogger = Logger.getLogger(pkg);
                pkgLogger.setLevel(Level.OFF);
                for (var handler : pkgLogger.getHandlers()) {
                    pkgLogger.removeHandler(handler);
                }
            }
        }
    }

    /**
     * Enables or disables logging at runtime — called from UI toggle.
     */
    public static void setEnabled(boolean enable) {
        configure(enable);
    }

    public static boolean isEnabled() {
        return enabled;
    }

    /**
     * Returns a logger for the given class.
     */
    public static Logger get(Class<?> clazz) {
        return Logger.getLogger(clazz.getName());
    }
}