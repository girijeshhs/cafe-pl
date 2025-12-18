<div align="center">

# ☕ Cafe Management System

*A modern command-line solution for small café operations*

[![Java](https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=java)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue?style=for-the-badge&logo=mysql)](https://www.mysql.com/)
[![JDBC](https://img.shields.io/badge/JDBC-Connector-green?style=for-the-badge)](https://dev.mysql.com/doc/connector-j/8.0/en/)

*Streamline your café operations with this robust Java application*

[🚀 Quick Start](#-quick-start) • [📖 Documentation](#-documentation) • [🔧 Setup](#installation)

---

</div>

## 📋 Table of Contents

- [✨ Overview](#-overview)
- [🚀 Quick Start](#-quick-start)
- [🔧 Installation](#installation)
- [🎮 Usage Guide](#usage-guide)
- [🏗️ Architecture](#architecture)
- [🗄️ Database Schema](#database-schema)
- [⚡ Features](#features)
- [📁 Project Structure](#project-structure)
- [🔍 Troubleshooting](#troubleshooting)
- [📚 Documentation](#-documentation)

---

## ✨ Overview

This **Cafe Management System** is a comprehensive command-line application designed to streamline café operations. Built with modern Java and MySQL, it provides an intuitive interface for managing customers, products, and orders with enterprise-grade reliability.

### 🎯 What You Can Do

<div align="center">

| 👥 **Customer Management** | 🛒 **Product Catalog** | 📦 **Order Processing** |
|:---------------------------|:-----------------------|:------------------------|
| • Add new customers<br>• Update contact info<br>• View customer history<br>• Remove inactive accounts | • Maintain menu items<br>• Set pricing<br>• Update availability<br>• Track inventory | • Process orders<br>• Calculate totals<br>• Generate bills<br>• Track order history |

</div>

### 🛠️ Technology Stack

<div align="center">

| Component | Version | Purpose |
|:----------|:--------|:--------|
| ![Java](https://img.shields.io/badge/Java-17+-orange?style=flat&logo=java) | 17+ | Core application logic |
| ![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue?style=flat&logo=mysql) | 8.0+ | Data persistence |
| ![JDBC](https://img.shields.io/badge/JDBC-8.x-green?style=flat) | 8.x | Database connectivity |

</div>

---

## 🚀 Quick Start

Get up and running in **5 minutes**!

### Prerequisites Check
```bash
# Verify Java installation
java -version  # Should show 17 or higher

# Verify MySQL installation
mysql --version  # Should show 8.0 or higher
```

### One-Command Setup
```bash
# 1. Setup database
mysql -u root -p < sql/database_setup.sql

# 2. Configure connection (edit src/DatabaseConnection.java)
# Update USER and PASSWORD constants

# 3. Compile and run
cd src && javac -cp "../lib/*:." *.java && java -cp "../lib/*:." App
```

🎉 **You're ready to manage your café!**

---

## 🔧 Installation

### Step 1: System Requirements

Ensure you have the following installed:

- **Java Development Kit (JDK) 17 or higher**
  ```bash
  java -version
  # Expected: java version "17.x.x" or higher
  ```

- **MySQL Server 8.0 or higher**
  ```bash
  mysql --version
  # Expected: mysql Ver 8.x.x or higher
  ```

- **MySQL JDBC Driver** *(Already included)*
  - Located in `lib/mysql-connector-java.jar`

### Step 2: Database Setup

Run the database initialization script:

```bash
mysql -u root -p < sql/database_setup.sql
```

This command creates:
- 🗄️ Database: `cafe_db`
- 📊 Tables: `customer`, `product`, `orders`, `order_details`
- 📝 Sample data for immediate testing

### Step 3: Configuration

Edit the database connection settings in `src/DatabaseConnection.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/cafe_db";
private static final String USER = "your_username";     // ← Update this
private static final String PASSWORD = "your_password"; // ← Update this
```

### Step 4: Compilation

Navigate to the source directory and compile:

```bash
cd src
javac -cp "../lib/*:." *.java
```

### Step 5: Launch

Start the application:

```bash
java -cp "../lib/*:." App
```

---

## 🎮 Usage Guide

### Main Menu

Upon launching, you'll see:

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

### 👥 Customer Management

#### Add Customer
1. Select `1` from main menu
2. Choose `1` to add customer
3. Enter customer name and phone number

#### View Customers
- Select `2` to display all customers with their IDs

#### Update Customer
- Select `3`, enter customer ID, then provide new details

#### Delete Customer
- Select `4` and enter customer ID
- ⚠️ **Note:** Cannot delete customers with existing orders

### 🛒 Product Management

#### Add Product
1. Select `2` from main menu
2. Choose `1` to add product
3. Enter product name and price
   - 💡 **Tip:** Price accepts formats like "240Rs" or "15.50"

#### View Products
- Select `2` to see complete product catalog

#### Update Product
- Select `3`, enter product ID, then modify details

#### Delete Product
- Select `4` and enter product ID
- ⚠️ **Note:** Cannot delete products referenced in orders

### 📦 Order Management

#### Create New Order
1. Select `3` from main menu
2. Choose `1` to create order
3. Enter customer ID (or press Enter for walk-in)
4. Add products:
   - Enter product ID
   - Enter quantity
   - Repeat for each item
   - Enter `0` when finished
5. System calculates total and saves order

#### View Orders
- Select `2` to see all orders with totals
- Select `3` to view detailed order items

#### Delete Order
- Select `4` and enter order ID to cancel

---

## 🏗️ Architecture

The application follows a clean **layered architecture** ensuring separation of concerns:

```
┌─────────────────────────────────────┐
│           🎯 App.java               │
│        (User Interface Layer)       │
│         Main Menu & Navigation      │
└─────────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────┐
│     🧠 Manager Classes Layer        │
│   CustomerManager │ ProductManager  │
│     OrderManager  │ (Business Logic)│
└─────────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────┐
│       🔌 DatabaseConnection         │
│       (Data Access Layer)           │
│         JDBC Connection Pool        │
└─────────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────┐
│          💾 MySQL Database          │
│     (Persistence & Storage Layer)   │
└─────────────────────────────────────┘
```

### Key Components

| Component | Responsibility | File |
|:----------|:---------------|:-----|
| **App.java** | Entry point, menu navigation | `src/App.java` |
| **CustomerManager** | Customer CRUD operations | `src/CustomerManager.java` |
| **ProductManager** | Product catalog management | `src/ProductManager.java` |
| **OrderManager** | Order processing & billing | `src/OrderManager.java` |
| **DatabaseConnection** | MySQL connection management | `src/DatabaseConnection.java` |

---

## 🗄️ Database Schema

### Tables Overview

<div align="center">

| Table | Purpose | Relationships |
|:------|:--------|:--------------|
| `customer` | Store customer information | Referenced by `orders` |
| `product` | Maintain product catalog | Referenced by `order_details` |
| `orders` | Order headers | References `customer`, referenced by `order_details` |
| `order_details` | Order line items | References `orders` & `product` |

</div>

### Detailed Schema

#### customer
| Column | Type | Constraints | Description |
|:-------|:-----|:------------|:------------|
| `id` | INT | PRIMARY KEY, AUTO_INCREMENT | Unique customer identifier |
| `name` | VARCHAR(100) | NOT NULL | Customer full name |
| `phone` | VARCHAR(15) | NOT NULL | Contact phone number |

#### product
| Column | Type | Constraints | Description |
|:-------|:-----|:------------|:------------|
| `id` | INT | PRIMARY KEY, AUTO_INCREMENT | Unique product identifier |
| `name` | VARCHAR(100) | NOT NULL | Product name |
| `price` | DECIMAL(10,2) | NOT NULL | Unit price |

#### orders
| Column | Type | Constraints | Description |
|:-------|:-----|:------------|:------------|
| `id` | INT | PRIMARY KEY, AUTO_INCREMENT | Unique order identifier |
| `customer_id` | INT | FOREIGN KEY, NULLABLE | Customer (NULL for walk-ins) |
| `total_price` | DECIMAL(10,2) | NOT NULL | Order total amount |

#### order_details
| Column | Type | Constraints | Description |
|:-------|:-----|:------------|:------------|
| `id` | INT | PRIMARY KEY, AUTO_INCREMENT | Unique detail identifier |
| `order_id` | INT | FOREIGN KEY, NOT NULL | Parent order |
| `product_id` | INT | FOREIGN KEY, NOT NULL | Ordered product |
| `quantity` | INT | NOT NULL | Quantity ordered |
| `price` | DECIMAL(10,2) | NOT NULL | Cached unit price |

---

## ⚡ Features

### 🔐 Security & Reliability

<div align="center">

| Feature | Description | Benefit |
|:--------|:------------|:--------|
| **🛡️ SQL Injection Protection** | Prepared statements for all queries | Prevents malicious attacks |
| **🔄 Transaction Management** | Atomic order processing | Data consistency guaranteed |
| **✅ Input Validation** | Comprehensive data checking | Prevents invalid data entry |
| **🔗 Referential Integrity** | Foreign key constraints | Maintains data relationships |

</div>

### 🎯 Business Logic

- **💰 Automatic Price Calculation** - Real-time total computation
- **📱 Flexible Customer Handling** - Support for walk-in customers
- **📊 Order History Tracking** - Complete audit trail
- **🔍 Duplicate Prevention** - Smart validation rules

### 🛠️ Technical Excellence

- **🏗️ Clean Architecture** - Separation of concerns
- **📦 Modular Design** - Easy maintenance and extension
- **⚡ Performance Optimized** - Efficient database queries
- **🔧 Error Handling** - Graceful failure management

---

## 📁 Project Structure

```
Cafe-Management_Java/
├── 📂 src/                          # Source code directory
│   ├── App.java                     # Main application entry point
│   ├── CustomerManager.java         # Customer operations
│   ├── ProductManager.java          # Product operations
│   ├── OrderManager.java            # Order processing
│   └── DatabaseConnection.java      # Database connectivity
├── 📂 lib/                          # External libraries
│   └── mysql-connector-java.jar     # MySQL JDBC driver
├── 📂 sql/                          # Database scripts
│   └── database_setup.sql           # Database initialization
├── 📂 docs/                         # Documentation
│   ├── PROJECT_REPORT.md            # Technical documentation
│   └── PROJECT_REPORT.docx          # Word format report
├── 📂 diagrams/                     # UML diagrams
│   ├── Use Case Diagram.svg         # System use cases
│   ├── Class Diagram.svg            # Class relationships
│   └── Sequence Diagram.svg         # Interaction flows
└── 📂 bin/                          # Compiled classes
    └── com/
        └── cafemanagement/          # Package structure
```

---

## 🔍 Troubleshooting

### Common Issues & Solutions

#### 🔌 Database Connection Issues

**Problem:** `Communications link failure`
```bash
# Solutions:
mysql -u root -p  # Verify MySQL is running
# Check DatabaseConnection.java credentials
# Ensure port 3306 is not blocked
```

#### 📚 JDBC Driver Not Found

**Problem:** `ClassNotFoundException: com.mysql.cj.jdbc.Driver`
```bash
# Solutions:
ls lib/mysql-connector-java.jar  # Verify file exists
# Check classpath: -cp "../lib/*:."
```

#### 🗑️ Deletion Restrictions

**Problem:** Cannot delete customer/product
```
This is expected behavior for data integrity.
Delete associated orders first, then try again.
```

#### 💰 Price Input Errors

**Problem:** `InputMismatchException` with prices
```
System accepts formats like:
✓ "240Rs" → 240.00
✓ "15.50" → 15.50
✓ "100" → 100.00
```

#### 🔐 Access Denied

**Problem:** `Access denied for user`
```sql
-- Grant permissions in MySQL:
GRANT ALL PRIVILEGES ON cafe_db.* TO 'username'@'localhost';
FLUSH PRIVILEGES;
```

---

## 📚 Documentation

### 📖 Additional Resources

- **[📄 Technical Report](docs/PROJECT_REPORT.md)** - Comprehensive system documentation
- **[📊 UML Diagrams](diagrams/)** - Visual system architecture
- **[🗄️ Database Scripts](sql/)** - SQL schema and sample data

### 🎓 Educational Value

This project demonstrates:
- ✅ **JDBC Fundamentals** - Connection, Statement, ResultSet
- ✅ **Database Design** - Normalization and relationships
- ✅ **Transaction Management** - ACID properties
- ✅ **Input Validation** - Data integrity techniques
- ✅ **Clean Architecture** - Separation of concerns

---

<div align="center">

## 🎉 Ready to Get Started?

Follow the [Quick Start](#-quick-start) guide above and you'll have your café management system running in minutes!

**Need Help?** Check the [troubleshooting](#troubleshooting) section or review the [technical documentation](docs/PROJECT_REPORT.md).

---

*Built with ❤️ for efficient café operations*

[![License: MIT](https://img.shields.io/badge/License-Educational-blue.svg)](LICENSE)

</div>
