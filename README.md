# 🎯 Lost and Found Portal  
_A JavaFX Desktop Application with MySQL Backend (XAMPP)_  

![JavaFX](https://img.shields.io/badge/JavaFX-UI-blue?style=for-the-badge&logo=java)  
![MySQL](https://img.shields.io/badge/MySQL-Database-orange?style=for-the-badge&logo=mysql)  
![NetBeans](https://img.shields.io/badge/NetBeans-IDE-blue?style=for-the-badge&logo=apachenetbeanside)  
![VS Code](https://img.shields.io/badge/VSCode-Editor-blue?style=for-the-badge&logo=visualstudiocode)  
![Status](https://img.shields.io/badge/Status-Active-brightgreen?style=for-the-badge)  

---

## 📌 Project Overview  
The **Lost and Found Portal** is a desktop application built using **JavaFX** for the UI and **MySQL (XAMPP)** as the backend.  
It helps users **report lost or found items** and manage their posts efficiently with a **modern, futuristic design**.  

✅ User Login & Registration  
✅ Create, View, Edit & Delete Posts (Lost/Found items)  
✅ Profile Management (Update contact info & password)  
✅ Beautiful, modern JavaFX UI  

---

## 🛠️ Tech Stack  
- **Frontend:** JavaFX (FXML, CSS for styling)  
- **Backend:** Java (JDBC for DB connectivity)  
- **Database:** MySQL (XAMPP Local Server)  
- **IDE:** NetBeans / VS Code  
- **Version Control:** Git & GitHub  

---

## 🎨 Features  
- 🔐 **Authentication**: Secure login & signup system.  
- 📝 **Post Creation**: Report lost or found items with details.  
- 📂 **My Posts**: View, edit, or delete your posts.  
- 👤 **Profile Management**: Update contact info & password.  
- 🖌 **Futuristic UI**: Gradient backgrounds, styled buttons, and responsive layout.  

---

## 🖥️ Installation & Setup  

### 1️⃣ Clone the Repository  
git clone https://github.com/YourUsername/LostAndFoundPortal.git
cd LostAndFoundPortal

2️⃣ Configure Database (MySQL/XAMPP)
Start XAMPP and enable Apache & MySQL.

Open phpMyAdmin and create a database:

sql
Copy
Edit
CREATE DATABASE lost_and_found;
Use the following SQL queries to create tables:

sql
Copy
Edit
-- Users Table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    contact_info VARCHAR(150),
    created_at DATE DEFAULT CURRENT_DATE
);

-- Items Table
CREATE TABLE items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,   -- Unique ID for each item
    type VARCHAR(50) NOT NULL,                -- "Lost" or "Found"
    item_name VARCHAR(100) NOT NULL,          -- Name of the item
    description TEXT NOT NULL,                -- Detailed description
    location VARCHAR(150) NOT NULL,           -- Where it was lost/found
    date_reported DATE NOT NULL,              -- Date of report
    userMail VARCHAR(100) NOT NULL,           -- Logged-in user's email (to link posts)
    contact_info VARCHAR(150) NOT NULL        -- Contact details (phone/email)
);
Update your DatabaseConnection.java:

java
Copy
Edit
private static final String URL = "jdbc:mysql://localhost:3306/lost_and_found";
private static final String USER = "root";
private static final String PASSWORD = "";
3️⃣ Run the Application
Open project in NetBeans or VS Code

Run Main.java

🎉 Enjoy your Lost & Found Portal!

👨‍🏫 Course Instructor
Md. Fazle Hasan Mihad
Lecturer, Department of CSE & CSIT

🤝 Contributing
Fork this repo 🍴

Create a new branch 🌱

Make changes and submit a pull request ✅

⭐ Show Support
If you found this helpful, give it a star ⭐ on GitHub!
