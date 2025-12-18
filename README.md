# Cafe Management System

A command-line application for managing café operations including customers, products, and orders. Built with Java and MySQL.

---

## Overview

This system provides a simple interface to:
- Manage customer records (add, view, update, delete)
- Maintain product catalog with pricing
- Process orders with automatic billing
- Track order history and details

**Technology Stack:**
- Java 17+
- MySQL 8.0+
- JDBC Connector 8.x

---

## Prerequisites

Before running this application, ensure you have:

1. **Java Development Kit (JDK) 17 or higher**
   ```bash
   java -version  # Should show 17 or higher
   ```

2. **MySQL Server 8.0 or higher**
   ```bash
   mysql --version
   ```

3. **MySQL JDBC Driver**  
   Already included in `lib/mysql-connector-java.jar`

---

## Installation

### 1. Database Setup

Run the provided SQL script to create the database and tables:

```bash
mysql -u root -p < sql/database_setup.sql
```

This creates:
- Database: `cafe_db`
- Tables: `customer`, `product`, `orders`, `order_details`
- Sample data for testing

### 2. Configure Database Connection

Edit `src/DatabaseConnection.java` and update your MySQL credentials:

```java
private static final String URL = "jdbc:mysql://localhost:3306/cafe_db";
private static final String USER = "root";           // Your MySQL username
private static final String PASSWORD = "yourpass";   // Your MySQL password
```

### 3. Compile the Application

Navigate to the `src` directory and compile:

```bash
cd src
javac -cp "../lib/*:." *.java
```

---

## Running the Application

From the `src` directory, run:

```bash
java -cp "../lib/*:." App
```

You'll see the main menu:

```
╔═══════════════════════════════════╗
║   CAFE MANAGEMENT SYSTEM          ║
╠═══════════════════════════════════╣
║  1. Customer Management           ║
║  2. Product Management            ║
║  3. Order Management              ║
║  4. Exit                          ║
╚═══════════════════════════════════╝
```

---

## Usage Guide

### Customer Management

**Add Customer**
1. Select option `1` from main menu
2. Choose `1` to add customer
3. Enter name and phone number

**View Customers**
- Select `2` to list all customers with their IDs

**Update Customer**
- Select `3`, enter customer ID, then provide new name/phone

**Delete Customer**
- Select `4` and enter customer ID
- Note: Cannot delete customers with existing orders

### Product Management

**Add Product**
1. Select option `2` from main menu
2. Choose `1` to add product
3. Enter product name and price
   - Price can include currency symbols (e.g., "240Rs")
   - System strips non-numeric characters automatically

**View Products**
- Select `2` to see all products with IDs and prices

**Update Product**
- Select `3`, enter product ID, then modify name or price

**Delete Product**
- Select `4` and enter product ID
- Note: Cannot delete products referenced in orders

### Order Management

**Create New Order**
1. Select option `3` from main menu
2. Choose `1` to create order
3. Enter customer ID (or press Enter to skip for walk-in)
4. Add products:
   - Enter product ID
   - Enter quantity
   - Repeat for each item
   - Enter `0` when done
5. System displays total bill and saves order

**View Orders**
- Select `2` to see all orders with totals
- Select `3` to view detailed order items

**Delete Order**
- Select `4` and enter order ID to cancel

---

## Architecture

The application follows a layered architecture:

```
App.java (Main Menu)
    ↓
Manager Classes (CustomerManager, ProductManager, OrderManager)
    ↓
DatabaseConnection (JDBC Layer)
    ↓
MySQL Database
```

**Key Components:**
- `App.java` - Entry point with main menu
- `CustomerManager.java` - Customer CRUD operations
- `ProductManager.java` - Product CRUD operations
- `OrderManager.java` - Order processing with transactions
- `DatabaseConnection.java` - MySQL connection management

---

## Database Schema

### Tables

**customer**
| Column | Type | Description |
|--------|------|-------------|
| id | INT | Primary key (auto-increment) |
| name | VARCHAR(100) | Customer name |
| phone | VARCHAR(15) | Contact number |

**product**
| Column | Type | Description |
|--------|------|-------------|
| id | INT | Primary key (auto-increment) |
| name | VARCHAR(100) | Product name |
| price | DECIMAL(10,2) | Unit price |

**orders**
| Column | Type | Description |
|--------|------|-------------|
| id | INT | Primary key (auto-increment) |
| customer_id | INT | Foreign key to customer (nullable) |
| total_price | DECIMAL(10,2) | Order total |

**order_details**
| Column | Type | Description |
|--------|------|-------------|
| id | INT | Primary key (auto-increment) |
| order_id | INT | Foreign key to orders |
| product_id | INT | Foreign key to product |
| quantity | INT | Quantity ordered |
| price | DECIMAL(10,2) | Cached product price |

---

## Features

### Transaction Management
Orders use database transactions to ensure data consistency. If any step fails during order creation, all changes are rolled back automatically.

### Input Validation
- Prices must be positive numbers
- Phone numbers validated for minimum length
- Quantities must be greater than zero
- Duplicate product IDs prevented in single order

### SQL Injection Prevention
All database queries use prepared statements to protect against SQL injection attacks.

### Referential Integrity
Foreign key constraints ensure:
- Orders cannot reference non-existent customers
- Order details cannot reference non-existent products or orders
- Cannot delete products/customers that have related orders

---

## Project Structure

```
Cafe-Management_Java/
├── src/                          # Java source files
│   ├── App.java
│   ├── CustomerManager.java
│   ├── ProductManager.java
│   ├── OrderManager.java
│   └── DatabaseConnection.java
├── lib/                          # JDBC driver
│   └── mysql-connector-java.jar
├── sql/                          # Database scripts
│   └── database_setup.sql
├── docs/                         # Documentation
│   ├── PROJECT_REPORT.md
│   └── PROJECT_REPORT.docx
├── diagrams/                     # UML diagrams
│   ├── Use Case Diagram.svg
│   ├── Class Diagram.svg
│   └── Sequence Diagram.svg
└── bin/                          # Compiled classes
```

---

## Troubleshooting

### Connection Refused Error
**Problem:** `Communications link failure`  
**Solution:** 
- Verify MySQL is running: `mysql -u root -p`
- Check connection details in `DatabaseConnection.java`
- Ensure MySQL port 3306 is not blocked

### ClassNotFoundException
**Problem:** `java.lang.ClassNotFoundException: com.mysql.cj.jdbc.Driver`  
**Solution:**
- Verify `mysql-connector-java.jar` is in `lib/` folder
- Check classpath includes `../lib/*` when compiling/running

### Foreign Key Constraint Error
**Problem:** Cannot delete customer/product  
**Solution:** This is expected behavior - delete associated orders first to maintain referential integrity

### Price Input Error
**Problem:** `InputMismatchException` when entering price  
**Solution:** System accepts formats like "240Rs" - currency symbols are stripped automatically. Enter positive numbers only.

---

## License

This project is created for educational purposes.

---

## Additional Documentation

- **Detailed Technical Report:** [docs/PROJECT_REPORT.md](docs/PROJECT_REPORT.md)
- **UML Diagrams:** [diagrams/](diagrams/) folder
- **Database Scripts:** [sql/](sql/) folder
