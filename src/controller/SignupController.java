package controller;

import database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.sql.*;
import javafx.scene.image.ImageView;

public class SignupController {
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button signupButton;
    @FXML private Hyperlink goToLogin;
    @FXML
    private ImageView logoImage;

    @FXML
    private void initialize() {
        signupButton.setOnAction(e -> handleSignup());
        goToLogin.setOnAction(e -> switchToLogin());
    }

private void handleSignup() {
    String name = nameField.getText().trim();
    String email = emailField.getText().trim();
    String password = passwordField.getText();

    // 1️⃣ Basic empty field check
    if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
        showAlert("Validation Error", "Please fill all fields.");
        return;
    }

    // 2️⃣ Email format validation
    if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
        showAlert("Validation Error", "Please enter a valid email address.");
        return;
    }

    // 3️⃣ Password strength validation
    if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
        showAlert("Weak Password",
                "Password must be at least 8 characters long and contain:\n" +
                        "- Uppercase letter\n" +
                        "- Lowercase letter\n" +
                        "- Number\n" +
                        "- Special character");
        return;
    }

    try {
        // ✅ 4️⃣ Try to connect to database
        Connection conn = DatabaseConnection.getConnection();

        // ✅ If DB doesn't exist, create it and table
        if (conn == null || conn.isClosed()) {
            System.out.println("Database not found. Creating database...");
            createDatabaseAndTable();
            conn = DatabaseConnection.getConnection();
        }

        // ✅ Ensure table exists
        createUsersTableIfNotExists(conn);

        // 5️⃣ Check if email already exists
        PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM users WHERE email=?");
        checkStmt.setString(1, email);
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next()) {
            showAlert("Signup Failed", "Email already registered.");
            return;
        }

        // 6️⃣ Insert new user
        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO users (name, email, password) VALUES (?, ?, ?)"
        );
        stmt.setString(1, name);
        stmt.setString(2, email);
        stmt.setString(3, password);
        stmt.executeUpdate();

        showAlert("Success", "Account created successfully!");
        switchToLogin();

    } catch (SQLException ex) {
        ex.printStackTrace();
        showAlert("Database Error", "Error while connecting to the database.");
    }
}

/**
 * Creates the database and users table if they don't exist
 */
private void createDatabaseAndTable() {
    String dbName = "lost_and_found"; // change to your DB name
    String serverUrl = "jdbc:mysql://localhost:3306/"; // MySQL server
    String user = "root";
    String pass = ""; // your MySQL password

    try (Connection conn = DriverManager.getConnection(serverUrl, user, pass);
         Statement stmt = conn.createStatement()) {

        // Create database if not exists
        stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
        System.out.println("Database created or already exists.");

    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Connect to the newly created DB and create the users table
    try (Connection conn = DriverManager.getConnection(serverUrl + dbName, user, pass);
         Statement stmt = conn.createStatement()) {

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(100) NOT NULL," +
                "email VARCHAR(100) NOT NULL UNIQUE," +
                "password VARCHAR(255) NOT NULL" +
                ")");
        System.out.println("Users table created or already exists.");

    } catch (SQLException e) {
        e.printStackTrace();
    }
}


private void createUsersTableIfNotExists(Connection conn) throws SQLException {
    Statement stmt = conn.createStatement();
    stmt.executeUpdate(
        "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(100), " +
                "email VARCHAR(100) UNIQUE, " +
                "password VARCHAR(100)" +
        ")"
    );
}


    
    private void switchToLogin() {
        try {
            Stage stage = (Stage) signupButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml")); // ✅ Ensure path is correct
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Failed to load Login screen.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
