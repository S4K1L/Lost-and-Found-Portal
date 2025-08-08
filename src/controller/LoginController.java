package controller;
import database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.sql.*;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Hyperlink goToSignup;

    public static String currentUserEmail = "";

    @FXML
    private void initialize() {
        loginButton.setOnAction(e -> handleLogin());
        goToSignup.setOnAction(e -> switchToSignup());
    }

    private void handleLogin() {
    String email = emailField.getText().trim();
    String password = passwordField.getText();

    // 1️⃣ Empty field check
    if (email.isEmpty() || password.isEmpty()) {
        showAlert("Validation Error", "Email and Password are required.");
        return;
    }

    // 2️⃣ Email format validation
    if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
        showAlert("Validation Error", "Please enter a valid email address.");
        return;
    }

    // 3️⃣ Password length validation
    if (password.length() < 8) {
        showAlert("Validation Error", "Password must be at least 8 characters long.");
        return;
    }

    // 4️⃣ Admin login check
    if (email.equals("admin@gmail.com") && password.equals("admin@root")) {
        currentUserEmail = email;
        loadScene("/view/AdminPage.fxml");
        return;
    }

    // 5️⃣ Normal user login
    try (Connection conn = DatabaseConnection.getConnection()) {
        if (conn == null || conn.isClosed()) {
            showAlert("Database Error", "Cannot connect to database.");
            return;
        }

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE email=? AND password=?");
        stmt.setString(1, email);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            currentUserEmail = email;
            loadScene("/view/Homepage.fxml");
        } else {
            showAlert("Login Failed", "Invalid email or password.");
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
        showAlert("Database Error", "Error while connecting to the database.");
    }
}



    private void switchToSignup() {
        loadScene("/view/Signup.fxml");
    }

    private void loadScene(String fxmlPath) {
        try {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
