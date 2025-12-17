import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        
        // Create manager objects
        CustomerManager customerManager = new CustomerManager(scanner);
        ProductManager productManager = new ProductManager(scanner);
        OrderManager orderManager = new OrderManager(scanner);
        
        // Main program loop
        while (true) {
            System.out.println("\n=================================");
            System.out.println("   CAFE MANAGEMENT SYSTEM");
            System.out.println("=================================");
            System.out.println("1. Customer Management");
            System.out.println("2. Product Management");
            System.out.println("3. Order Management");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1:
                    customerManager.showMenu();
                    break;
                case 2:
                    productManager.showMenu();
                    break;
                case 3:
                    orderManager.showMenu();
                    break;
                case 4:
                    System.out.println("\nThank you for using Cafe Management System!");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}
