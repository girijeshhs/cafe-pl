-- Create database
CREATE DATABASE IF NOT EXISTS cafe_db;

-- Use the database
USE cafe_db;

-- Create Customer table
CREATE TABLE IF NOT EXISTS Customer (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL
);

-- Create Product table
CREATE TABLE IF NOT EXISTS Product (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);

-- Create Orders table
CREATE TABLE IF NOT EXISTS Orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id)
);

-- Create Order_Details table
CREATE TABLE IF NOT EXISTS Order_Details (
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

-- Insert sample data for testing

-- Clear existing data (in correct order due to foreign keys)
DELETE FROM Order_Details;
DELETE FROM Orders;
DELETE FROM Product;
DELETE FROM Customer;

-- Reset auto-increment counters
ALTER TABLE Customer AUTO_INCREMENT = 1;
ALTER TABLE Product AUTO_INCREMENT = 1;
ALTER TABLE Orders AUTO_INCREMENT = 1;

-- Sample customers
INSERT INTO Customer (name, phone) VALUES 
('Amit Kumar', '9876543210'),
('Priya Sharma', '9876543211'),
('Rahul Verma', '9876543212'),
('Anjali Gupta', '9876543213'),
('Vikram Singh', '9876543214'),
('Neha Patel', '9876543215');

-- Sample products
INSERT INTO Product (product_name, price) VALUES 
('Cappuccino', 120.00),
('Espresso', 80.00),
('Latte', 100.00),
('Matcha', 150.00),
('Cold Coffee', 130.00),
('Tea', 60.00),
('Green Tea', 70.00),
('Sandwich', 90.00),
('Burger', 150.00),
('Pizza Slice', 180.00),
('Pasta', 200.00),
('Cake', 110.00),
('Cookie', 40.00),
('Muffin', 80.00),
('Croissant', 95.00);

-- Sample orders
INSERT INTO Orders (customer_id) VALUES (1), (2), (3), (4);

-- Sample order details
INSERT INTO Order_Details (order_id, product_id, quantity) VALUES 
(1, 1, 2),  -- Amit ordered 2 Cappuccinos
(1, 8, 1),  -- Amit ordered 1 Sandwich
(2, 2, 1),  -- Priya ordered 1 Espresso
(2, 12, 2), -- Priya ordered 2 Cakes
(3, 4, 1),  -- Rahul ordered 1 Matcha
(3, 11, 1), -- Rahul ordered 1 Pasta
(4, 5, 2),  -- Anjali ordered 2 Cold Coffees
(4, 13, 3); -- Anjali ordered 3 Cookies

-- Display success message
SELECT 'Database setup completed successfully!' AS Message;
