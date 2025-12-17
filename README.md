# Cafe Management System

A simple console-based Java application for managing a cafe using JDBC and MySQL.

## Project Overview

This is a beginner-friendly academic project that demonstrates basic CRUD operations using Java and MySQL database.

## Features

- **Customer Management**: Add, view, update, and delete customers
- **Product Management**: Add, view, update, and delete products
- **Order Management**: Create orders, add products to orders, view orders, and delete orders

## Technology Stack

- Java
- JDBC (Java Database Connectivity)
- MySQL Database

## Database Setup

1. Make sure MySQL is installed and running on your system
2. Run the `database_setup.sql` file to create the database and tables:
   ```bash
   mysql -u root -p < database_setup.sql
   ```
3. Update the database credentials in `DatabaseConnection.java`:
   - Default user: `root`
   - Default password: `` (empty)
   - Change these according to your MySQL setup

## Database Structure

- **cafe_db** (Database)
  - **Customer** (customer_id, name, phone)
  - **Product** (product_id, product_name, price)
  - **Orders** (order_id, customer_id)
  - **Order_Details** (order_id, product_id, quantity)

## How to Run

1. Ensure MySQL JDBC driver is in the `lib` folder
2. Update database credentials in `DatabaseConnection.java` if needed
3. Compile and run `App.java`:
   ```bash
   cd src
   javac -cp "../lib/*:." *.java
   java -cp "../lib/*:." App
   ```

## Project Structure

```
Cafe-Management_Java/
├── src/
│   ├── App.java                 # Main entry point
│   ├── DatabaseConnection.java  # Database connection handler
│   ├── CustomerManager.java     # Customer CRUD operations
│   ├── ProductManager.java      # Product CRUD operations
│   └── OrderManager.java        # Order CRUD operations
├── lib/
│   └── mysql-connector-java.jar # MySQL JDBC driver
├── database_setup.sql           # SQL script to setup database
└── README.md
```

## Menu Structure

### Main Menu
1. Customer Management
2. Product Management
3. Order Management
4. Exit

### Customer Management
1. Add Customer
2. View Customers
3. Update Customer
4. Delete Customer
5. Back to Main Menu

### Product Management
1. Add Product
2. View Products
3. Update Product
4. Delete Product
5. Back to Main Menu

### Order Management
1. Create Order
2. Add Product to Order
3. View Orders
4. Delete Order
5. Back to Main Menu

## Important Notes

- This is an academic project focused on simplicity and clarity
- No advanced design patterns are used
- All operations are console-based (no GUI)
- Suitable for demonstration in viva/practical exams

## Prerequisites

- JDK 8 or higher
- MySQL Server
- MySQL JDBC Connector (place in `lib` folder)

## Author

Academic Project - Cafe Management System
