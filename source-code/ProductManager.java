import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ProductManager {
    
    private Scanner scanner;
    
    public ProductManager(Scanner scanner) {
        this.scanner = scanner;
    }
    
    // Show Product Management Menu
    public void showMenu() {
        while (true) {
            System.out.println("\n=== Product Management ===");
            System.out.println("1. Add Product");
            System.out.println("2. View Products");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    viewProducts();
                    break;
                case 3:
                    updateProduct();
                    break;
                case 4:
                    deleteProduct();
                    break;
                case 5:
                    return; // Back to main menu
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
    
    // Add a new product
    private void addProduct() {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        
        double price = 0;
        boolean validInput = false;
        
        while (!validInput) {
            System.out.print("Enter product price: ");
            String priceInput = scanner.nextLine().trim();
            
            // Remove common currency symbols and letters
            priceInput = priceInput.replaceAll("[^0-9.]", "");
            
            try {
                price = Double.parseDouble(priceInput);
                if (price <= 0) {
                    System.out.println("Price must be greater than 0. Please try again.");
                } else {
                    validInput = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid price format. Please enter a valid number.");
            }
        }
        
        Connection conn = DatabaseConnection.getConnection();
        
        if (conn != null) {
            try {
                String sql = "INSERT INTO Product (product_name, price) VALUES (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, name);
                pstmt.setDouble(2, price);
                
                int rows = pstmt.executeUpdate();
                
                if (rows > 0) {
                    System.out.println("Product added successfully!");
                }
                
                pstmt.close();
                
            } catch (SQLException e) {
                System.out.println("Error adding product!");
                e.printStackTrace();
            } finally {
                DatabaseConnection.closeConnection(conn);
            }
        }
    }
    
    // View all products
    private void viewProducts() {
        Connection conn = DatabaseConnection.getConnection();
        
        if (conn != null) {
            try {
                String sql = "SELECT * FROM Product";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();
                
                System.out.println("\n=== Product List ===");
                System.out.println("ID\tProduct Name\t\tPrice");
                System.out.println("-----------------------------------");
                
                while (rs.next()) {
                    int id = rs.getInt("product_id");
                    String name = rs.getString("product_name");
                    double price = rs.getDouble("price");
                    
                    System.out.println(id + "\t" + name + "\t\t₹" + price);
                }
                
                rs.close();
                pstmt.close();
                
            } catch (SQLException e) {
                System.out.println("Error viewing products!");
                e.printStackTrace();
            } finally {
                DatabaseConnection.closeConnection(conn);
            }
        }
    }
    
    // Update existing product
    private void updateProduct() {
        System.out.print("Enter product ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        System.out.print("Enter new product name: ");
        String name = scanner.nextLine();
        
        double price = 0;
        boolean validInput = false;
        
        while (!validInput) {
            System.out.print("Enter new price: ");
            String priceInput = scanner.nextLine().trim();
            
            // Remove common currency symbols and letters
            priceInput = priceInput.replaceAll("[^0-9.]", "");
            
            try {
                price = Double.parseDouble(priceInput);
                if (price <= 0) {
                    System.out.println("Price must be greater than 0. Please try again.");
                } else {
                    validInput = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid price format. Please enter a valid number.");
            }
        }
        
        Connection conn = DatabaseConnection.getConnection();
        
        if (conn != null) {
            try {
                String sql = "UPDATE Product SET product_name = ?, price = ? WHERE product_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, name);
                pstmt.setDouble(2, price);
                pstmt.setInt(3, id);
                
                int rows = pstmt.executeUpdate();
                
                if (rows > 0) {
                    System.out.println("Product updated successfully!");
                } else {
                    System.out.println("Product not found!");
                }
                
                pstmt.close();
                
            } catch (SQLException e) {
                System.out.println("Error updating product!");
                e.printStackTrace();
            } finally {
                DatabaseConnection.closeConnection(conn);
            }
        }
    }
    
    // Delete a product
    private void deleteProduct() {
        System.out.print("Enter product ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        Connection conn = DatabaseConnection.getConnection();
        
        if (conn != null) {
            try {
                // First, check if product is used in any orders
                String checkSql = "SELECT COUNT(*) FROM Order_Details WHERE product_id = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setInt(1, id);
                ResultSet rs = checkStmt.executeQuery();
                
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("⚠️  Cannot delete this product!");
                    System.out.println("   This product is part of existing orders.");
                    System.out.println("   Please remove it from all orders first.");
                    rs.close();
                    checkStmt.close();
                    return;
                }
                
                rs.close();
                checkStmt.close();
                
                // If no orders reference this product, proceed with deletion
                String sql = "DELETE FROM Product WHERE product_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                
                int rows = pstmt.executeUpdate();
                
                if (rows > 0) {
                    System.out.println("✓ Product deleted successfully!");
                } else {
                    System.out.println("Product not found!");
                }
                
                pstmt.close();
                
            } catch (SQLException e) {
                System.out.println("Error deleting product!");
                e.printStackTrace();
            } finally {
                DatabaseConnection.closeConnection(conn);
            }
        }
    }
}
