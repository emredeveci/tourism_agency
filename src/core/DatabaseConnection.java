package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    //CRITERIA 6 (with Singleton Pattern)
    //database connection parameters
    private static final String URL = "jdbc:postgresql://localhost:5432/tourismagency";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "deveci";

    //singleton instance
    private static DatabaseConnection instance;

    //private constructor to prevent instantiation
    private DatabaseConnection() {
    }

    //method to get the singleton instance, also ensuring thread safety
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    //method to get database connection
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    //method to close database connection
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
