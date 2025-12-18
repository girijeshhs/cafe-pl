import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class OrderManager {
    
    private Scanner scanner;
    
    public OrderManager(Scanner scanner) {
        this.scanner = scanner;
    }
    
    // Show Order Management Menu
    public void showMenu() {
        while (true) {
            System.out.println("\n=== Order Management ===");
            System.out.println("1. Create Order");
            System.out.println("2. Add Product to Order");
            System.out.println("3. View Orders");
            System.out.println("4. Delete Order");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1:
                    createOrder();
                    break;
                case 2:
                    addProductToOrder();
                    break;
                case 3:
                    viewOrders();
                    break;
                case 4:
                    deleteOrder();
                    break;
                case 5:
                    return; // Back to main menu
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
    
    // Create a new order
    private void createOrder() {
        System.out.print("Enter customer ID: ");
        int customerId = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        Connection conn = DatabaseConnection.getConnection();
        
        if (conn != null) {
            try {
                // Check if customer exists
                String checkSql = "SELECT * FROM Customer WHERE customer_id = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setInt(1, customerId);
                ResultSet rs = checkStmt.executeQuery();
                
                if (!rs.next()) {
                    System.out.println("Customer not found!");
                    rs.close();
                    checkStmt.close();
                    return;
                }
                rs.close();
                checkStmt.close();
                
                // Create order
                String sql = "INSERT INTO Orders (customer_id) VALUES (?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, customerId);
                
                int rows = pstmt.executeUpdate();
                
                if (rows > 0) {
                    System.out.println("Order created successfully!");
                }
                
                pstmt.close();
                
            } catch (SQLException e) {
                System.out.println("Error creating order!");
                e.printStackTrace();
            } finally {
                DatabaseConnection.closeConnection(conn);
            }
        }
    }
    
    // Add product to an existing order
    private void addProductToOrder() {
        System.out.print("Enter order ID: ");
        int orderId = scanner.nextInt();
        
        System.out.print("Enter product ID: ");
        int productId = scanner.nextInt();
        
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        Connection conn = DatabaseConnection.getConnection();
        
        if (conn != null) {
            try {
                // Check if order exists
                String checkOrderSql = "SELECT * FROM Orders WHERE order_id = ?";
                PreparedStatement checkOrderStmt = conn.prepareStatement(checkOrderSql);
                checkOrderStmt.setInt(1, orderId);
                ResultSet rsOrder = checkOrderStmt.executeQuery();
                
                if (!rsOrder.next()) {
                    System.out.println("Order not found!");
                    rsOrder.close();
                    checkOrderStmt.close();
                    return;
                }
                rsOrder.close();
                checkOrderStmt.close();
                
                // Check if product exists
                String checkProductSql = "SELECT * FROM Product WHERE product_id = ?";
                PreparedStatement checkProductStmt = conn.prepareStatement(checkProductSql);
                checkProductStmt.setInt(1, productId);
                ResultSet rsProduct = checkProductStmt.executeQuery();
                
                if (!rsProduct.next()) {
                    System.out.println("Product not found!");
                    rsProduct.close();
                    checkProductStmt.close();
                    return;
                }
                rsProduct.close();
                checkProductStmt.close();
                
                // Add product to order
                String sql = "INSERT INTO Order_Details (order_id, product_id, quantity) VALUES (?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, orderId);
                pstmt.setInt(2, productId);
                pstmt.setInt(3, quantity);
                
                int rows = pstmt.executeUpdate();
                
                if (rows > 0) {
                    System.out.println("Product added to order successfully!");
                }
                
                pstmt.close();
                
            } catch (SQLException e) {
                System.out.println("Error adding product to order!");
                e.printStackTrace();
            } finally {
                DatabaseConnection.closeConnection(conn);
            }
        }
    }
    
    // View all orders with details
    private void viewOrders() {
        Connection conn = DatabaseConnection.getConnection();
        
        if (conn != null) {
            try {
                String sql = "SELECT o.order_id, c.name AS customer_name, " +
                           "p.product_name, od.quantity " +
                           "FROM Orders o " +
                           "JOIN Customer c ON o.customer_id = c.customer_id " +
                           "LEFT JOIN Order_Details od ON o.order_id = od.order_id " +
                           "LEFT JOIN Product p ON od.product_id = p.product_id " +
                           "ORDER BY o.order_id";
                
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();
                
                System.out.println("\n=== Order List ===");
                System.out.println("Order ID | Customer Name | Product Name | Quantity");
                System.out.println("--------------------------------------------------------");
                
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    String customerName = rs.getString("customer_name");
                    String productName = rs.getString("product_name");
                    
                    // Handle null values for orders without products
                    if (productName == null) {
                        productName = "No products";
                    }
                    
                    int quantity = rs.getInt("quantity");
                    if (rs.wasNull()) {
                        quantity = 0;
                    }
                    
                    System.out.println(orderId + "\t | " + customerName + "\t | " + 
                                     productName + "\t | " + quantity);
                }
                
                rs.close();
                pstmt.close();
                
            } catch (SQLException e) {
                System.out.println("Error viewing orders!");
                e.printStackTrace();
            } finally {
                DatabaseConnection.closeConnection(conn);
            }
        }
    }
    
    // Delete an order
    private void deleteOrder() {
        System.out.print("Enter order ID to delete: ");
        int orderId = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        Connection conn = DatabaseConnection.getConnection();
        
        if (conn != null) {
            try {
                // First delete order details
                String deleteDetailsSql = "DELETE FROM Order_Details WHERE order_id = ?";
                PreparedStatement deleteDetailsStmt = conn.prepareStatement(deleteDetailsSql);
                deleteDetailsStmt.setInt(1, orderId);
                deleteDetailsStmt.executeUpdate();
                deleteDetailsStmt.close();
                
                // Then delete the order
                String deleteOrderSql = "DELETE FROM Orders WHERE order_id = ?";
                PreparedStatement deleteOrderStmt = conn.prepareStatement(deleteOrderSql);
                deleteOrderStmt.setInt(1, orderId);
                
                int rows = deleteOrderStmt.executeUpdate();
                
                if (rows > 0) {
                    System.out.println("Order deleted successfully!");
                } else {
                    System.out.println("Order not found!");
                }
                
                deleteOrderStmt.close();
                
            } catch (SQLException e) {
                System.out.println("Error deleting order!");
                e.printStackTrace();
            } finally {
                DatabaseConnection.closeConnection(conn);
            }
        }
    }
}
