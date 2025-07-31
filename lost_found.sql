CREATE DATABASE IF NOT EXISTS lost_found_db;
USE lost_found_db;

CREATE TABLE IF NOT EXISTS items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(10),
    item_name VARCHAR(100),
    description TEXT,
    location VARCHAR(100),
    date_reported DATE,
    status VARCHAR(20),
    contact_info VARCHAR(100),
    image_path VARCHAR(255)
);