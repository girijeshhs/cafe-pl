<div align="center">

# ☕ Cafe Management System

### *A Modern Command-Line Solution for Small Café Operations*

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql)
![JDBC](https://img.shields.io/badge/JDBC-Connector-green?style=for-the-badge)

---

</div>

## 📖 What Is This Project About?

Imagine a small café still using **handwritten tickets** and **scattered spreadsheets** to track orders, customers, and inventory. This project replaces that chaos with a **simple, reliable, menu-driven application** that runs entirely in your terminal.

### 🎯 The Core Idea

Instead of juggling papers and risking errors, café staff can:
- ✅ Store customer information in one place
- ✅ Maintain a product catalog with prices
- ✅ Record orders with multiple items
- ✅ Generate accurate bills instantly
- ✅ Track inventory changes automatically

**No internet required. No expensive licenses. Just Java, MySQL, and straightforward workflows.**

---

## 🏗️ How Does It Work? (The Big Picture)

Think of this system as **three connected layers**, like a sandwich:

```
┌─────────────────────────────────────────┐
│   👤 CLI Menus (User Interface)         │  ← Staff types commands here
├─────────────────────────────────────────┤
│   ⚙️  Manager Classes (Business Logic)  │  ← Validates data, handles rules
├─────────────────────────────────────────┤
│   💾 Database Layer (MySQL Storage)     │  ← Stores all information safely
└─────────────────────────────────────────┘
```

### 🔄 Example Workflow: Placing an Order

Let's walk through what happens when a staff member creates an order:

1. **Staff selects "Place Order"** from the menu
2. **OrderManager validates** the input (quantities > 0, prices valid)
3. **Database starts a transaction** (all-or-nothing approach)
4. **System inserts** order header, line items, and updates stock
5. **If everything succeeds**, changes are saved; **if not**, everything rolls back
6. **Bill is printed** to the screen instantly

> 💡 **Key Point**: The transaction ensures you never have half-saved orders cluttering your database.

---

## 🎨 Visual Architecture

Here's how the main components talk to each other:

```
        App.java (Entry Point)
             │
     ┌───────┼───────┐
     │       │       │
CustomerMgr ProductMgr OrderMgr
     │       │       │
     └───────┼───────┘
             │
    DatabaseConnection
             │
        MySQL Database
```

**Each manager** handles one domain (customers, products, orders) and uses **prepared statements** to talk safely to MySQL—no SQL injection risks!

---

## 🗂️ Database Design (What Gets Stored)

Our MySQL database has **four simple tables**:

| Table | Columns | Purpose |
|-------|---------|---------|
| **Customer** | `id`, `name`, `phone` | Stores customer details |
| **Product** | `id`, `name`, `price` | Catalog of items sold |
| **Orders** | `id`, `customer_id`, `date` | Order headers |
| **Order_Details** | `order_id`, `product_id`, `qty` | What was in each order |

### 🔗 Relationships
- One order → Many products (via `Order_Details`)
- One customer → Many orders

---

## 🚀 Quick Start Guide

### Step 1️⃣: Setup the Database

```bash
# Run the setup script to create tables
mysql -u root -p < sql/database_setup.sql
```

This creates the `cafe_db` database with all four tables ready to use.

### Step 2️⃣: Configure Connection

Open `src/DatabaseConnection.java` and update:

```java
private static final String URL = "jdbc:mysql://localhost:3306/cafe_db";
private static final String USER = "root";        // ← Your MySQL username
private static final String PASSWORD = "yourpass"; // ← Your MySQL password
```

### Step 3️⃣: Compile and Run

```bash
# Navigate to source folder
cd src

# Compile everything
javac -cp "../lib/*:." *.java

# Run the application
java -cp "../lib/*:." App
```

---

## 🎮 How to Use (The Menus Explained)

When you start the app, you'll see a **Main Menu** with three options:

### 📋 Main Menu
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
- **Add Customer**: Save name and phone number
- **View All**: List everyone in the system
- **Update**: Change phone or name
- **Delete**: Remove a customer (if no orders exist)

### 🛒 Product Management
- **Add Product**: Enter name and price
- **View All**: See the full menu catalog
- **Update**: Change prices or names
- **Delete**: Remove discontinued items

### 📦 Order Management
- **Create Order**: Link to a customer (or leave blank)
- **Add Items**: Pick products and quantities
- **View All**: See order history
- **Delete**: Cancel incorrect orders

---

## 💡 Key Features (What Makes This Special)

### ✨ Transaction Safety
Every order is wrapped in a **database transaction**. If step 3 fails while adding 5 items, **all 5 are rolled back**—no orphaned data.

### 🛡️ Input Validation
The system checks:
- ✅ Prices must be positive
- ✅ Phone numbers meet minimum length
- ✅ Quantities can't be zero

This stops bad data **before** it reaches the database.

### 📊 Simple Reporting
Need to know daily totals? The system generates:
- Revenue summaries
- Most ordered items
- Customer order counts

All from the same clean database.

### 🔒 Security Basics
- **Prepared statements** prevent SQL injection
- **No passwords in code** (stored in config files)
- **Local-only** (no web vulnerabilities)

---

## 📂 Project Structure (Where Everything Lives)

```
Cafe-Management_Java/
│
├── 📁 src/                      ← All Java source code
│   ├── App.java                 # Starting point (main method)
│   ├── CustomerManager.java     # Handles customer CRUD
│   ├── ProductManager.java      # Handles product CRUD
│   ├── OrderManager.java        # Handles orders + billing
│   └── DatabaseConnection.java  # MySQL connection setup
│
├── 📁 lib/                      ← External libraries
│   └── mysql-connector-java.jar # JDBC driver for MySQL
│
├── 📁 sql/                      ← Database scripts
│   └── database_setup.sql       # Creates tables
│
├── 📁 docs/                     ← Documentation
│   ├── PROJECT_REPORT.md        # Full technical report
│   └── PROJECT_REPORT.docx      # Word version
│
├── 📁 diagrams/                 ← UML diagrams
│   ├── Use Case Diagram.svg
│   ├── Class Diagram.svg
│   └── Sequence Diagram.svg
│
└── 📁 bin/                      ← Compiled .class files
```

---

## 🎓 Perfect for Academic Presentations

### When Explaining to Teachers, Mention:

1. **Why CLI?**  
   *"No need for complex UI frameworks. Runs on any lab machine with Java and MySQL. Easy to demo via SSH."*

2. **Why No Frameworks?**  
   *"Using plain JDBC keeps the code transparent—every SQL query is visible, making it ideal for learning database interactions."*

3. **Why Transactions?**  
   *"Orders involve multiple inserts. Transactions ensure data consistency—either everything saves or nothing does."*

4. **Real-World Application**  
   *"Small cafés with intermittent internet can run this locally without cloud dependencies or subscription fees."*

---

## 🔧 Technical Requirements

| Component | Version | Purpose |
|-----------|---------|---------|
| **Java** | 17+ | Core programming language |
| **MySQL** | 8.0+ | Database for persistent storage |
| **JDBC Connector** | 8.x | Bridge between Java and MySQL |
| **OS** | Any (macOS, Windows, Linux) | Cross-platform compatible |

---

## 🎯 Design Decisions (Why We Built It This Way)

### ✅ Command-Line Interface
- **Pro**: Works over SSH, no graphics libraries needed
- **Pro**: Fast to navigate for experienced users
- **Con**: Not suitable for non-technical staff

### ✅ No Web Frontend
- **Pro**: Zero deployment complexity (no servers)
- **Pro**: No security risks from web attacks
- **Con**: Single-user at a time

### ✅ Local MySQL Database
- **Pro**: Works offline, no cloud costs
- **Pro**: Full data ownership
- **Con**: Requires manual backups

---

## 🚦 What This Project Does Well

| Strength | Explanation |
|----------|-------------|
| 🟢 **Simple & Teachable** | Code is easy to read—no magic frameworks |
| 🟢 **Transactional** | Orders are atomic (all-or-nothing) |
| 🟢 **Validated** | Bad inputs are rejected early |
| 🟢 **Modular** | Each manager handles one domain |

## 🚧 Known Limitations

| Limitation | Reason |
|------------|--------|
| 🟡 **Single User** | No concurrent access handling |
| 🟡 **CLI Only** | Not suitable for non-technical users |
| 🟡 **Manual Backups** | No automated backup scheduling |
| 🟡 **English Only** | No internationalization |

---

## 📝 Sample Demo Script (For Your Presentation)

> *"Good morning. Today I'll demonstrate a Cafe Management System built with Java and MySQL.*
>
> *The problem: Small cafés rely on paper tickets, which cause errors and slow down service.*
>
> *Our solution: A command-line application that stores customers, products, and orders in MySQL.*
>
> *Let me show you the flow. First, I add a customer—here's 'John Doe' with his phone number. Now I add a product—'Cappuccino' at ₹120.*
>
> *To create an order: Select customer, add products with quantities. The system validates everything, wraps it in a transaction, and generates a bill.*
>
> *If any step fails—say, an invalid quantity—the entire order rolls back. This keeps the database consistent.*
>
> *The architecture uses three layers: CLI menus for input, manager classes for validation, and JDBC for database access. All SQL uses prepared statements to prevent injection attacks.*
>
> *This project demonstrates core JDBC concepts, transaction management, and clean separation of concerns—all without external frameworks.*
>
> *Thank you. I'm ready for questions."*

---

## 🏆 Learning Outcomes

By building and presenting this project, you demonstrate understanding of:

- ✅ **JDBC fundamentals** (Connection, Statement, ResultSet)
- ✅ **Transaction management** (commit, rollback)
- ✅ **Database design** (normalization, foreign keys)
- ✅ **Input validation** (preventing bad data)
- ✅ **Modular code organization** (separation of concerns)
- ✅ **Error handling** (try-catch, logging)

---

## 📚 Additional Resources

- **Full Technical Report**: See [docs/PROJECT_REPORT.md](docs/PROJECT_REPORT.md)
- **UML Diagrams**: Check [diagrams/](diagrams/) folder
- **SQL Scripts**: All in [sql/](sql/) folder

---

<div align="center">

### 🎉 Ready to Run?

Follow the **Quick Start Guide** above and you'll have the system running in 5 minutes!

**Questions?** Check the technical report or review the inline comments in the source code.

---

*Made with ☕ for academic demonstration*

</div>
