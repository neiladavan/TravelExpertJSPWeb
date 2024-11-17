package org.example.travelexpertsproductjsp.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DatabaseUtil {

    private static final String PROPERTIES_FILE_PATH = "c:\\connection.properties";
    private static final String DB_URL_PROPERTY = "mariadb_url";
    private static final String DB_USER_PROPERTY = "user";
    private static final String DB_PASSWORD_PROPERTY = "password";
    private static final String DB_DRIVER_CLASS = "org.mariadb.jdbc.Driver";

    /**
     * Retrieves a database connection using properties specified in an external properties file.
     *
     * @return A Connection object to the database.
     * @throws RuntimeException if there is an issue reading the properties file or establishing a connection.
     */
    public static Connection getConnection() {
        // Establish the database connection
        try {
            Properties properties = loadProperties();
            String url = properties.getProperty(DB_URL_PROPERTY);
            String user = properties.getProperty(DB_USER_PROPERTY);
            String password = properties.getProperty(DB_PASSWORD_PROPERTY);

            Class.forName(DB_DRIVER_CLASS);
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            LoggerUtil.logError("Error establishing the database connection:", e);
            throw new RuntimeException("Error establishing the database connection: " + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            LoggerUtil.logError("Database driver class not found:", e);
            throw new RuntimeException("Database driver class not found: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            LoggerUtil.logError("An error occurred while processing the request.", e);
            throw new RuntimeException("Error loading database properties from " + PROPERTIES_FILE_PATH + ": " + e.getMessage(), e);
        }
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(PROPERTIES_FILE_PATH)) {
            properties.load(fis);
        } catch (IOException e) {
            LoggerUtil.logError("An error occurred while processing the request.", e);
            throw new RuntimeException("Error loading database properties from " + PROPERTIES_FILE_PATH + ": " + e.getMessage(), e);
        }
        return properties;
    }

    /**
     * Creates a PreparedStatement with the given SQL query and parameters.
     *
     * @param conn   The Connection object to the database.
     * @param sql    The SQL query to be executed.
     * @param params The parameters to be inserted into the SQL query.
     * @return A PreparedStatement object ready for execution.
     * @throws SQLException if there is an issue preparing the SQL statement.
     */
    public static PreparedStatement createPreparedStatement(Connection conn, String sql, Object... params) throws SQLException {
        if (conn == null) {
            throw new SQLException("Connection is null");
        }

        PreparedStatement stmt = conn.prepareStatement(sql);

        // Set the parameters in the prepared statement
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]); // Parameters are 1-based index in PreparedStatement
        }
        System.out.println(stmt); // Print the prepared statement for debugging purposes

        return stmt;
    }
}

