# Project Report: Cafe Management System

**Date:** 17 December 2025  
**Author:** [Your Name]  
**Subject:** Comprehensive Technical Report on Cafe Management System Implementation

---

## 1. Executive Summary

The **Cafe Management System** is a console-based software application designed to streamline the operational workflows of a cafe. Developed using **Java** and **MySQL**, the system provides a robust platform for managing customer data, product inventories, and order processing. This project demonstrates the practical application of Object-Oriented Programming (OOP) principles and Java Database Connectivity (JDBC) to create a persistent, data-driven application. The system effectively replaces manual record-keeping with an automated, efficient, and error-free digital solution.

## 2. Introduction

### 2.1 Background
Traditional cafe management often relies on paper-based systems or disconnected spreadsheets, leading to inefficiencies, data redundancy, and human error. As business volume grows, the need for an automated system becomes critical to maintain service quality and operational control.

### 2.2 Objectives
The primary objectives of this project are:
*   To develop a user-friendly Command Line Interface (CLI) for cafe administration.
*   To implement full CRUD (Create, Read, Update, Delete) operations for core business entities.
*   To utilize a relational database (MySQL) for secure and persistent data storage.
*   To demonstrate the integration of Java application logic with database management systems via JDBC.

### 2.3 Scope
The current scope of the project includes:
*   **Customer Management:** Registration and maintenance of customer details.
*   **Product Management:** Cataloging menu items and pricing.
*   **Order Management:** Processing customer orders and calculating totals.
*   **Data Persistence:** Storing all records in a MySQL database.

---

## 3. System Analysis and Requirements

### 3.1 Functional Requirements
*   **User Interface:** The system shall provide a text-based menu for navigation.
*   **Input Validation:** The system shall handle user inputs gracefully.
*   **Data Integrity:** The system shall ensure referential integrity between Orders, Customers, and Products.

### 3.2 Software Requirements
*   **Operating System:** Platform Independent (Windows/macOS/Linux).
*   **Programming Language:** Java (JDK 8 or higher).
*   **Database:** MySQL Server (5.7 or 8.0).
*   **Drivers:** MySQL Connector/J (JDBC Driver).
*   **IDE/Editor:** VS Code, IntelliJ IDEA, or Eclipse.

---

## 4. System Design

### 4.1 System Architecture
The application follows a modular architecture separating the presentation layer, business logic, and data access layer.

*   **Presentation Layer:** `App.java` handles user interaction and menu display.
*   **Business Logic Layer:** `CustomerManager`, `ProductManager`, and `OrderManager` classes contain the core functionality.
*   **Data Access Layer:** `DatabaseConnection` manages the connection to the MySQL database using JDBC.

### 4.2 Database Schema (ER Design)
The database `cafe_db` is normalized to ensure efficient data storage.

| Table Name | Primary Key | Foreign Keys | Description |
| :--- | :--- | :--- | :--- |
| **Customer** | `customer_id` | None | Stores customer name and contact info. |
| **Product** | `product_id` | None | Stores menu items and unit prices. |
| **Orders** | `order_id` | `customer_id` | Links an order to a specific customer. |
| **Order_Details** | `(order_id, product_id)` | `order_id`, `product_id` | Junction table for line items (Many-to-Many resolution). |

### 4.3 Class Structure
*   **`App`**: The entry point containing the `main` method and the primary event loop.
*   **`DatabaseConnection`**: A utility class implementing the Singleton pattern (conceptually) to provide a static connection object.
*   **`CustomerManager`**: Handles `INSERT`, `SELECT`, `UPDATE`, `DELETE` for customers.
*   **`ProductManager`**: Handles inventory operations.
*   **`OrderManager`**: Manages complex transactions involving both `Orders` and `Order_Details` tables.

---

## 5. Implementation Details

### 5.1 Technology Stack
*   **Core Java:** Used for application logic, control flow, and input handling (`java.util.Scanner`).
*   **JDBC API:** Used for executing SQL queries (`java.sql.Connection`, `PreparedStatement`, `ResultSet`).
*   **MySQL:** Used as the backend RDBMS.

### 5.2 Key Code Highlights
**Secure Database Connection:**
The system uses `PreparedStatement` to prevent SQL Injection attacks and improve performance through pre-compilation.

```java
// Example: Adding a Customer safely
String query = "INSERT INTO Customer (name, phone) VALUES (?, ?)";
PreparedStatement pstmt = connection.prepareStatement(query);
pstmt.setString(1, name);
pstmt.setString(2, phone);
pstmt.executeUpdate();
```

**Transaction Management:**
Order creation involves multiple steps (creating the order header and adding line items). Future iterations will implement `connection.setAutoCommit(false)` to ensure atomicity across these operations.

---

## 6. Testing and Results

### 6.1 Testing Strategy
*   **Unit Testing:** Each manager class was tested individually to verify CRUD operations.
*   **Integration Testing:** Verified the flow of data from the Java application to the MySQL database and back.

### 6.2 Test Cases
1.  **Add Customer:** Verified that a new customer appears in the database with the correct ID.
2.  **Create Order:** Verified that an order is linked to the correct customer and contains the correct products.
3.  **Data Persistence:** Restarted the application to confirm that data remains available.

---

## 7. Future Enhancements

To further improve the system, the following enhancements are proposed:
1.  **Graphical User Interface (GUI):** Migration to JavaFX or a Web-based interface (Spring Boot + React) for better user experience.
2.  **Authentication & Authorization:** Implementing login roles (Admin vs. Staff) for security.
3.  **Reporting Module:** Generating PDF receipts and sales reports (Daily/Monthly).
4.  **Connection Pooling:** Integrating HikariCP to manage database connections efficiently under load.

## 8. Conclusion

The Cafe Management System successfully meets its design objectives. It provides a reliable, efficient, and scalable foundation for managing cafe operations. The project serves as a strong demonstration of full-stack development concepts using Java and SQL, highlighting the importance of database normalization and modular code design.

---
*End of Report*
