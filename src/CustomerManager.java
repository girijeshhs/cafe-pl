import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CustomerManager {
    
    private Scanner scanner;
    
    public CustomerManager(Scanner scanner) {
        this.scanner = scanner;
    }
    
    // Show Customer Management Menu
    public void showMenu() {
        while (true) {
            System.out.println("\n=== Customer Management ===");
            System.out.println("1. Add Customer");
            System.out.println("2. View Customers");
            System.out.println("3. Update Customer");
            System.out.println("4. Delete Customer");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1:
                    addCustomer();
                    break;
                case 2:
                    viewCustomers();
                    break;
                case 3:
                    updateCustomer();
                    break;
                case 4:
                    deleteCustomer();
                    break;
                case 5:
                    return; // Back to main menu
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
    
    // Add a new customer
    private void addCustomer() {
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter customer phone: ");
        String phone = scanner.nextLine();
        
        Connection conn = DatabaseConnection.getConnection();
        
        if (conn != null) {
            try {
                String sql = "INSERT INTO Customer (name, phone) VALUES (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, name);
                pstmt.setString(2, phone);
                
                int rows = pstmt.executeUpdate();
                
                if (rows > 0) {
                    System.out.println("Customer added successfully!");
                }
                
                pstmt.close();
                
            } catch (SQLException e) {
                System.out.println("Error adding customer!");
                e.printStackTrace();
            } finally {
                DatabaseConnection.closeConnection(conn);
            }
        }
    }
    
    // View all customers
    private void viewCustomers() {
        Connection conn = DatabaseConnection.getConnection();
        
        if (conn != null) {
            try {
                String sql = "SELECT * FROM Customer";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();
                
                System.out.println("\n=== Customer List ===");
                System.out.println("ID\tName\t\tPhone");
                System.out.println("-----------------------------------");
                
                while (rs.next()) {
                    int id = rs.getInt("customer_id");
                    String name = rs.getString("name");
                    String phone = rs.getString("phone");
                    
                    System.out.println(id + "\t" + name + "\t\t" + phone);
                }
                
                rs.close();
                pstmt.close();
                
            } catch (SQLException e) {
                System.out.println("Error viewing customers!");
                e.printStackTrace();
            } finally {
                DatabaseConnection.closeConnection(conn);
            }
        }
    }
    
    // Update existing customer
    private void updateCustomer() {
        System.out.print("Enter customer ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter new phone: ");
        String phone = scanner.nextLine();
        
        Connection conn = DatabaseConnection.getConnection();
        
        if (conn != null) {
            try {
                String sql = "UPDATE Customer SET name = ?, phone = ? WHERE customer_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, name);
                pstmt.setString(2, phone);
                pstmt.setInt(3, id);
                
                int rows = pstmt.executeUpdate();
                
                if (rows > 0) {
                    System.out.println("Customer updated successfully!");
                } else {
                    System.out.println("Customer not found!");
                }
                
                pstmt.close();
                
            } catch (SQLException e) {
                System.out.println("Error updating customer!");
                e.printStackTrace();
            } finally {
                DatabaseConnection.closeConnection(conn);
            }
        }
    }
    
    // Delete a customer
    private void deleteCustomer() {
        System.out.print("Enter customer ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        Connection conn = DatabaseConnection.getConnection();
        
        if (conn != null) {
            try {
                String sql = "DELETE FROM Customer WHERE customer_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                
                int rows = pstmt.executeUpdate();
                
                if (rows > 0) {
                    System.out.println("Customer deleted successfully!");
                } else {
                    System.out.println("Customer not found!");
                }
                
                pstmt.close();
                
            } catch (SQLException e) {
                System.out.println("Error deleting customer!");
                e.printStackTrace();
            } finally {
                DatabaseConnection.closeConnection(conn);
            }
        }
    }
}
