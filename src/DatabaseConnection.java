import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    
    // Database credentials
    // Tip: You can also set these using environment variables:
    // DB_URL, DB_USER, DB_PASSWORD
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/cafe_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "Yuichi1290"; // If your root has a password, set it here or use DB_PASSWORD

    private static String envOrDefault(String key, String fallback) {
        String value = System.getenv(key);
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }
        return value.trim();
    }
    
    // Method to get database connection
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish connection
            String url = envOrDefault("DB_URL", DEFAULT_URL);
            String user = envOrDefault("DB_USER", DEFAULT_USER);
            String password = envOrDefault("DB_PASSWORD", DEFAULT_PASSWORD);
            conn = DriverManager.getConnection(url, user, password);
            
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
            System.out.println("Add the MySQL Connector JAR inside the lib folder.");
        } catch (SQLException e) {
            // Common case: wrong username/password
            if (e.getErrorCode() == 1045) {
                System.out.println("Connection failed: Access denied (wrong username/password).");
                System.out.println("Fix: update DEFAULT_PASSWORD in DatabaseConnection.java or set DB_PASSWORD env var.");
            } else {
                System.out.println("Connection failed: " + e.getMessage());
            }
        }
        return conn;
    }
    
    // Method to close connection
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
