package controller;

import database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.sql.*;
import java.time.LocalDate;

public class EditController {

    @FXML private ComboBox<String> typeComboBox;
    @FXML private TextField nameField, locationField, contactField;
    @FXML private DatePicker datePicker;
    @FXML private TextArea descriptionArea;
    @FXML private Button updateButton, cancelButton;

    private int postId = MyPostsController.editingItemId;

    @FXML
    private void initialize() {
        typeComboBox.getItems().addAll("Lost", "Found");
        loadPostDetails();

        updateButton.setOnAction(e -> updatePost());
        cancelButton.setOnAction(e -> goBack());
    }

    private void loadPostDetails() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM items WHERE item_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                typeComboBox.setValue(rs.getString("type"));
                nameField.setText(rs.getString("item_name"));
                locationField.setText(rs.getString("location"));
                descriptionArea.setText(rs.getString("description"));
                contactField.setText(rs.getString("contact_info"));
                datePicker.setValue(rs.getDate("date_reported").toLocalDate());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updatePost() {
        String type = typeComboBox.getValue();
        String name = nameField.getText().trim();
        String location = locationField.getText().trim();
        LocalDate date = datePicker.getValue();
        String description = descriptionArea.getText().trim();
        String contact = contactField.getText().trim();

        if (type == null || name.isEmpty() || location.isEmpty() || date == null || contact.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "All fields are required!").showAndWait();
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String updateQuery = "UPDATE items SET type=?, item_name=?, location=?, date_reported=?, description=?, contact_info=? WHERE item_id=?";
            PreparedStatement stmt = conn.prepareStatement(updateQuery);
            stmt.setString(1, type);
            stmt.setString(2, name);
            stmt.setString(3, location);
            stmt.setDate(4, java.sql.Date.valueOf(date));
            stmt.setString(5, description);
            stmt.setString(6, contact);
            stmt.setInt(7, postId);

            stmt.executeUpdate();
            new Alert(Alert.AlertType.INFORMATION, "Post updated successfully!").showAndWait();
            goBack();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void goBack() {
        try {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/view/MyPosts.fxml"));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
