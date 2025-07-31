package controller;

import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import java.sql.*;

public class MyPostsController {

    @FXML private ListView<String> myPostListView;
    @FXML private Button editButton, deleteButton, backButton, newPostButton;
    public static int editingItemId = -1;

    private ObservableList<String> myPosts = FXCollections.observableArrayList();
    private int[] postIds;

    @FXML
    private void initialize() {
        loadMyPosts();

        // Enable/disable edit & delete buttons based on selection
        myPostListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            boolean selected = newVal.intValue() >= 0;
            editButton.setDisable(!selected);
            deleteButton.setDisable(!selected);
        });

        // ✅ Button handlers
        deleteButton.setOnAction(e -> deleteSelectedPost());
        editButton.setOnAction(e -> editSelectedPost());
        backButton.setOnAction(e -> switchScene("/view/Homepage.fxml"));
        newPostButton.setOnAction(e -> switchScene("/view/Create.fxml")); // Ensure correct case
    }

    private void loadMyPosts() {
    myPosts.clear();

    try (Connection conn = DatabaseConnection.getConnection()) {
        System.out.println("Fetching posts for user: " + LoginController.currentUserEmail);

        String query = "SELECT * FROM items WHERE LOWER(TRIM(userMail)) = LOWER(TRIM(?)) ORDER BY date_reported DESC";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, LoginController.currentUserEmail.trim());
        ResultSet rs = stmt.executeQuery();

        // Collect posts
        postIds = new int[1000]; // Temporary size; will be trimmed later
        int index = 0;

        while (rs.next()) {
            if (index >= postIds.length) break; // Prevent overflow
            postIds[index] = rs.getInt("item_id");

            String post = String.format("[%s] %s\nLocation: %s\nReported: %s",
                    rs.getString("type"),
                    rs.getString("item_name"),
                    rs.getString("location"),
                    rs.getDate("date_reported"));
            myPosts.add(post);
            index++;
        }

        // ✅ Trim postIds array
        if (index > 0) {
            int[] trimmedIds = new int[index];
            System.arraycopy(postIds, 0, trimmedIds, 0, index);
            postIds = trimmedIds;
        } else {
            postIds = new int[0]; // No posts
            System.out.println("No posts found for this user.");
        }

        myPostListView.setItems(myPosts);

    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    private void deleteSelectedPost() {
        int selectedIndex = myPostListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0 || selectedIndex >= postIds.length) return;

        int postId = postIds[selectedIndex];

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM items WHERE item_id = ?");
            stmt.setInt(1, postId);
            stmt.executeUpdate();

            myPosts.remove(selectedIndex);
            myPostListView.getItems().remove(selectedIndex);
            new Alert(Alert.AlertType.INFORMATION, "Post deleted successfully.").showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void switchScene(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) myPostListView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            System.out.println("✅ Switched to: " + fxml);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load: " + fxml + "\n" + e.getMessage()).showAndWait();
        }
    }
    
    private void editSelectedPost() {
    int selectedIndex = myPostListView.getSelectionModel().getSelectedIndex();
    if (selectedIndex < 0 || selectedIndex >= postIds.length) {
        new Alert(Alert.AlertType.WARNING, "Please select a post to edit.").showAndWait();
        return;
    }

    editingItemId = postIds[selectedIndex]; // Store selected post ID
    switchScene("/view/Edit.fxml"); // Open the edit page
}
}
