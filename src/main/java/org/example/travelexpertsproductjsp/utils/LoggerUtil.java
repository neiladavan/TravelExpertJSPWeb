package org.example.travelexpertsproductjsp.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerUtil {
    private static final Logger logger = Logger.getLogger(LoggerUtil.class.getName());

    static {
        try {
            // Ensure the logs directory exists
            Path logDir = Paths.get("C:\\logs");
            System.out.println(">>>>>" + logDir.toFile().getAbsolutePath());
            if (Files.notExists(logDir)) {
                Files.createDirectories(logDir);
            }

            // Set up the FileHandler to write logs to a specific file
            FileHandler fileHandler = new FileHandler(logDir.toFile().getAbsolutePath()+"\\error.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false); // Disable console output

            // Test log entry to verify setup
            logger.info("Logger setup successfully.");
        } catch (IOException e) {
            System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>>>Failed to set up logger: " + e.getMessage());
            e.printStackTrace(); // Handle potential IOException
        }
    }

    public static void logError(String message, Exception exception) {
        System.out.println(">>>>>" + exception.getMessage());
        logger.severe(message + " - Exception: " + exception.getMessage());
    }
}
