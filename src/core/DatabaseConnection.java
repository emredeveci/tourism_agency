package core;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    //CRITERIA 6 (with Singleton Pattern)
    //database connection parameters

    private static final String PROPERTIES_FILE_PATH = "database.properties";

    // Singleton instance
    private static DatabaseConnection instance;

    // Database connection parameters
    private String url;
    private String username;
    private String password;

    // Private constructor to prevent instantiation
    private DatabaseConnection() {
        loadProperties();
    }

    // Method to load properties from file
    private void loadProperties() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(PROPERTIES_FILE_PATH)) {
            properties.load(fis);
            url = properties.getProperty("db.url");
            username = properties.getProperty("db.username");
            password = properties.getProperty("db.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to get the singleton instance, also ensuring thread safety
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Method to get database connection
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    // Method to close database connection
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

