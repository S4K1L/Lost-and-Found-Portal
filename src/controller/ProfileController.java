package controller;

import database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.sql.*;

public class ProfileController {

    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private PasswordField passwordField;
    @FXML private Button updatePasswordButton;
    @FXML private Button backButton;

    private String currentEmail = LoginController.currentUserEmail;

    // ✅ FIXED: Added @FXML annotation so it is automatically called
    @FXML
    private void initialize() {
        loadProfile();
        updatePasswordButton.setOnAction(e -> updatePassword());
        backButton.setOnAction(e -> switchScene("/view/Homepage.fxml")); // ✅ Now works
    }

    private void loadProfile() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE LOWER(TRIM(email)) = LOWER(TRIM(?))";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, currentEmail.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nameLabel.setText("Name: " + rs.getString("name"));
                emailLabel.setText("Email: " + rs.getString("email"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void updatePassword() {
        String newPassword = passwordField.getText().trim();
        if (newPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please enter a new password!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE users SET password = ? WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, newPassword);
            stmt.setString(2, currentEmail);
            int updated = stmt.executeUpdate();

            if (updated > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Password changed successfully!");
                passwordField.clear();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void switchScene(String fxmlPath) {
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type, msg);
        alert.showAndWait();
    }
}
