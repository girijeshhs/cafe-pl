import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    
    // Database credentials
    // Tip: You can also set these using environment variables:
    // DB_URL, DB_USER, DB_PASSWORD
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/cafe_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    // URL used only for creating database/tables when missing (no DB name in URL)
    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
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
        return getConnection(false);
    }

    // Internal helper so we can retry once after creating database
    private static Connection getConnection(boolean retried) {
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
            } else if (e.getErrorCode() == 1049 && !retried) { // 1049 = Unknown database
                System.out.println("Database 'cafe_db' not found. Creating it now...");
                createDatabaseAndTables();
                // Try once more after creating database
                return getConnection(true);
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

    // Create database and required tables if they do not exist
    // This runs only when MySQL says "Unknown database 'cafe_db'".
    private static void createDatabaseAndTables() {
        String user = envOrDefault("DB_USER", DEFAULT_USER);
        String password = envOrDefault("DB_PASSWORD", DEFAULT_PASSWORD);

        try (Connection conn = DriverManager.getConnection(SERVER_URL, user, password);
             Statement stmt = conn.createStatement()) {

            // Create database
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS cafe_db");
            stmt.executeUpdate("USE cafe_db");

            // Create tables (same structure as database_setup.sql)
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Customer (" +
                               "customer_id INT PRIMARY KEY AUTO_INCREMENT," +
                               "name VARCHAR(100) NOT NULL," +
                               "phone VARCHAR(15) NOT NULL" +
                               ")");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Product (" +
                               "product_id INT PRIMARY KEY AUTO_INCREMENT," +
                               "product_name VARCHAR(100) NOT NULL," +
                               "price DECIMAL(10, 2) NOT NULL" +
                               ")");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Orders (" +
                               "order_id INT PRIMARY KEY AUTO_INCREMENT," +
                               "customer_id INT NOT NULL," +
                               "FOREIGN KEY (customer_id) REFERENCES Customer(customer_id)" +
                               ")");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Order_Details (" +
                               "order_id INT NOT NULL," +
                               "product_id INT NOT NULL," +
                               "quantity INT NOT NULL," +
                               "PRIMARY KEY (order_id, product_id)," +
                               "FOREIGN KEY (order_id) REFERENCES Orders(order_id)," +
                               "FOREIGN KEY (product_id) REFERENCES Product(product_id)" +
                               ")");

            System.out.println("Database 'cafe_db' and tables are ready.");

        } catch (SQLException e) {
            System.out.println("Failed to create database/tables: " + e.getMessage());
        }
    }
}
