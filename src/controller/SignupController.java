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

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Validation Error", "Please fill all fields.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null || conn.isClosed()) {
                showAlert("Database Error", "Cannot connect to database.");
                return;
            }

            PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM users WHERE email=?");
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                showAlert("Signup Failed", "Email already registered.");
                return;
            }

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
