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

-- Sample customers
INSERT INTO Customer (name, phone) VALUES 
('Amit Kumar', '9876543210'),
('Priya Sharma', '9876543211'),
('Rahul Verma', '9876543212');

-- Sample products
INSERT INTO Product (product_name, price) VALUES 
('Coffee', 50.00),
('Tea', 30.00),
('Sandwich', 80.00),
('Burger', 120.00),
('Pizza', 200.00);

-- Sample orders
INSERT INTO Orders (customer_id) VALUES (1), (2);

-- Sample order details
INSERT INTO Order_Details (order_id, product_id, quantity) VALUES 
(1, 1, 2),
(1, 3, 1),
(2, 2, 3),
(2, 4, 2);

-- Display success message
SELECT 'Database setup completed successfully!' AS Message;
