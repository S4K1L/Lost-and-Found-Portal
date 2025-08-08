package controller;

import database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;

import java.sql.*;
import java.time.LocalDate;

public class CreateController {

    @FXML private ComboBox<String> typeComboBox;
    @FXML private TextField nameField, locationField, contactField;
    @FXML private TextArea descriptionArea;
    @FXML private DatePicker datePicker;
    @FXML private Button submitButton, backButton;

    @FXML
    private void initialize() {
        // Dropdown for Lost/Found
        typeComboBox.getItems().addAll("Lost", "Found");

        submitButton.setOnAction(e -> handleSubmit());
        backButton.setOnAction(e -> switchScene("/view/MyPosts.fxml"));
    }

    private void handleSubmit() {
        String type = typeComboBox.getValue();
        String name = nameField.getText().trim();
        String location = locationField.getText().trim();
        LocalDate date = datePicker.getValue();
        String description = descriptionArea.getText().trim();
        String contact = contactField.getText().trim();

        if (type == null || name.isEmpty() || location.isEmpty() || date == null || description.isEmpty() || contact.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "All fields are required.");
            return;
        }

        try {
            Connection conn = DatabaseConnection.getConnection();

            if (conn == null || conn.isClosed()) {
                System.out.println("Database not found. Creating database and table...");
                createDatabaseAndTable();
                conn = DatabaseConnection.getConnection(); // reconnect after creation
            }

            createItemsTableIfNotExists(conn);

            String query = "INSERT INTO items (type, item_name, location, date_reported, description, contact_info, userMail) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, type);
            stmt.setString(2, name);
            stmt.setString(3, location);
            stmt.setDate(4, Date.valueOf(date));
            stmt.setString(5, description);
            stmt.setString(6, contact);
            stmt.setString(7, LoginController.currentUserEmail);

            stmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Post created successfully!");

            switchScene("/view/MyPosts.fxml");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not save the post.");
        }
    }

    private void createDatabaseAndTable() {
        String dbName = "lost_and_found"; // your DB name
        String serverUrl = "jdbc:mysql://localhost:3306/";
        String user = "root";
        String pass = ""; // your MySQL password

        try (Connection conn = DriverManager.getConnection(serverUrl, user, pass);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
            System.out.println("Database created or already exists.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createItemsTableIfNotExists(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS items (" +
                    "item_id INT PRIMARY KEY AUTO_INCREMENT," +
                    "type VARCHAR(20) NOT NULL," +
                    "item_name VARCHAR(255) NOT NULL," +
                    "location VARCHAR(255) NOT NULL," +
                    "date_reported DATE NOT NULL," +
                    "description TEXT NOT NULL," +
                    "contact_info VARCHAR(255) NOT NULL," +
                    "userMail VARCHAR(255) NOT NULL" +
                    ")");
            System.out.println("Items table created or already exists.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void switchScene(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
