# Cafe Management System
## Console-Driven Java Application for Single-Location Cafés

**Presented by:** [Your Name]  
**Date:** December 18, 2025

---

## Slide 1: Problem Statement

### Current Challenges in Small Cafés

**Manual Processes Create Inefficiencies:**
- Handwritten tickets lead to billing errors and delays during peak hours
- Ad hoc spreadsheets cause data duplication and reconciliation problems
- Staff waste time copying totals from paper to digital formats
- Difficult to track inventory, customer history, and daily sales
- No central system for order management and reporting

**Impact:**
- ❌ Lost revenue due to billing mistakes
- ❌ Time wasted on manual data entry
- ❌ Inability to analyze trends or track popular items
- ❌ Inconsistent workflows when staff rotate

**Need:** A predictable, low-cost digital solution that runs on existing hardware without additional licensing or cloud dependencies

---

## Slide 2: Project Overview & Objectives

### What is the Cafe Management System?

A **console-driven Java application** backed by MySQL that replaces manual processes with a single, menu-driven workflow.

### Key Objectives

1. **Standardize Operations** - Consistent workflow for orders, billing, and inventory
2. **Reduce Errors** - Eliminate manual data entry and transcription mistakes
3. **Centralize Data** - Single source of truth for customers, products, and orders
4. **Low Cost & Offline** - Runs on existing laptops without cloud services or licensing fees
5. **Easy to Deploy** - Minimal configuration, no specialized hardware required

### Scope
✅ Customer management  
✅ Product catalog with pricing  
✅ Order capture with multiple items  
✅ Invoice generation  
✅ Daily reporting and summaries

---

## Slide 3: System Architecture - Block Diagram

### Four-Layer Architecture

![Architecture Diagram](cafe_architecture.xml)

**Layer 1: Presentation Layer (CLI Interface)**
- App.java - Main controller
- Customer, Product, Order, and Billing menus

**Layer 2: Business Logic Layer (Managers)**
- CustomerManager - Validation, duplicate checks
- ProductManager - Price validation, stock management
- OrderManager - Transactions, invoice generation

**Layer 3: Data Access Layer (DAO)**
- DatabaseConnection - JDBC management, connection pooling
- Customer, Product, Order DAOs - CRUD operations with prepared statements

**Layer 4: Database Layer (MySQL)**
- customers, products, orders, order_items tables
- Foreign key relationships ensure data integrity

---

## Slide 4: Technology Stack

### Simple, Stable, and Widely Available

| Component | Technology | Purpose |
|-----------|------------|---------|
| **Language** | Java 17 | CLI application logic |
| **Database** | MySQL 8.0 | Local data storage |
| **Build Tool** | Maven | Dependency management |
| **JDBC Driver** | Connector/J | Database connectivity |
| **Testing** | JUnit | Unit tests for validation |

### Why This Stack?

✅ **No licensing costs** - All open-source components  
✅ **Offline capable** - No cloud dependencies  
✅ **Standard tools** - Familiar to students and small teams  
✅ **Transparent** - Plain SQL, no framework abstraction  
✅ **Reproducible** - Runs on any laptop with Java & MySQL

---

## Slide 5: Key Features

### 1. Customer Management
- Add, view, update, delete customer records
- Phone-based duplicate detection
- Simple validation (phone length, email format)

### 2. Product Catalog
- Maintain products with names, prices, stock levels
- Enforce positive pricing
- Basic pagination for large catalogs

### 3. Order Processing
- Multi-item orders with line-by-line entry
- **Transactional integrity** - All items commit or rollback together
- Automatic total calculation
- Invoice generation at completion

### 4. Reporting & Analytics
- Daily sales totals
- Top-selling products
- Customer order history
- End-of-day reconciliation reports

---

## Slide 6: Implementation Highlights

### Core Design Principles

**1. Layered Architecture**
- Clear separation: CLI → Managers → DAOs → Database
- Changes in one layer don't affect others
- Easy to test and maintain

**2. Transaction Management**
```java
// Pseudo-code for order creation
begin transaction
  INSERT INTO orders (customer_id, total)
  INSERT INTO order_items (order_id, product_id, quantity)
  UPDATE products SET stock = stock - quantity
commit or rollback
```

**3. Input Validation**
- Validate at CLI edge before database calls
- Positive prices, non-zero quantities, valid phone formats
- Clear error messages for operators

**4. Security**
- Prepared statements prevent SQL injection
- Credentials stored outside source control
- Logs exclude sensitive data

---

## Slide 7: Database Schema

### Four Core Entities

**customers**
- customer_id (PK)
- name, phone, email

**products**
- product_id (PK)
- name, price, stock

**orders**
- order_id (PK)
- customer_id (FK)
- order_date, total

**order_items**
- item_id (PK)
- order_id (FK) → orders
- product_id (FK) → products
- quantity, price

### Relationships
- One customer → Many orders
- One order → Many order_items
- One product → Many order_items

---

## Slide 8: Testing & Validation

### Testing Strategy

**1. Unit Testing**
- JUnit for validation logic and helper methods
- Test positive/negative price validation
- Phone number format checks

**2. Integration Testing**
- End-to-end CRUD flows for customers, products, orders
- Transaction rollback scenarios (partial order failures)
- Duplicate customer detection

**3. Cross-Platform Testing**
- Verified on macOS and Windows
- Same Java/MySQL versions across environments

### Key Test Cases
✅ Reject negative prices  
✅ Rollback partial orders on failure  
✅ Prevent duplicate customers (same phone)  
✅ Calculate correct order totals  
✅ Generate accurate invoices

---

## Slide 9: Results & Benefits

### Operational Improvements

**Before (Manual Process):**
- ❌ Handwritten tickets prone to errors
- ❌ Manual reconciliation takes hours
- ❌ No order history or trend analysis
- ❌ Staff need extensive training

**After (CMS Implementation):**
- ✅ Consistent order entry workflow
- ✅ Daily totals generated in minutes
- ✅ Traceable inventory adjustments
- ✅ Staff learn system in one walkthrough

### Measured Outcomes
- **Faster order capture** - Linear prompts reduce confusion
- **Clearer reconciliation** - Totals match database records
- **Better inventory tracking** - Every item decrement tied to an order
- **Reduced training time** - Simple CLI menus, no hidden shortcuts

### Cost Comparison
- **Commercial POS:** $50-200/month + hardware
- **CMS:** $0 recurring costs, runs on existing laptops

---

## Slide 10: Conclusion & Future Work

### Project Success

✅ **Delivered** a working CLI system that replaces manual café processes  
✅ **Validated** with seed data and real-world scenarios  
✅ **Demonstrated** transactional integrity and data validation  
✅ **Documented** setup, deployment, and operational procedures

### Limitations (By Design)
- CLI-only interface (no GUI)
- No payment gateway integration
- Manual backups required
- Single-location deployment

### Future Enhancements

**Near-term (Within Current Stack):**
- Automated backup scripts
- CSV export for advanced reporting
- Enhanced CLI help text and error recovery
- Seed data packs for demos

**Long-term (Optional):**
- Lightweight web interface (reuses same managers/DAOs)
- Email receipt generation
- Basic loyalty tracking
- Multi-location support

---

## Thank You!

### Questions?

**Key Takeaways:**
1. Simple CLI + MySQL solution replaces fragmented manual processes
2. Layered architecture ensures maintainability and testability
3. Zero licensing costs, offline-capable, transparent design
4. Suitable for small cafés and academic coursework

**Project Repository:** [GitHub Link]  
**Contact:** [Your Email]

---
