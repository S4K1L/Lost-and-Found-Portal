package controller;

import database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class AdminController {

    @FXML private ListView<String> postListView;
    @FXML private Button deleteButton;
    @FXML private Button logoutButton;

    private ObservableList<String> myPosts = FXCollections.observableArrayList();
    private int[] postIds;
    private int selectedPostId = -1;

    @FXML
    private void initialize() {
        loadAllPosts();

        postListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            int index = newVal.intValue();
            if (index >= 0 && index < postIds.length) {
                selectedPostId = postIds[index];
                deleteButton.setDisable(false);
            } else {
                selectedPostId = -1;
                deleteButton.setDisable(true);
            }
        });

        deleteButton.setOnAction(e -> deletePost());
        logoutButton.setOnAction(e -> logout());
    }

    /**
     * Loads all posts from the items table with the email of the user who posted them.
     */
    private void loadAllPosts() {
        myPosts.clear();

        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Fetching ALL posts for admin");

            String query = "SELECT * FROM items ORDER BY date_reported DESC";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            postIds = new int[1000];
            int index = 0;

            while (rs.next()) {
                if (index >= postIds.length) break;

                postIds[index] = rs.getInt("item_id");

                String post = String.format("[%s] %s\nLocation: %s\nReported: %s\nBy: %s",
                        rs.getString("type"),
                        rs.getString("item_name"),
                        rs.getString("location"),
                        rs.getDate("date_reported"),
                        rs.getString("userMail"));
                myPosts.add(post);
                index++;
            }

            // Trim postIds array
            if (index > 0) {
                int[] trimmedIds = new int[index];
                System.arraycopy(postIds, 0, trimmedIds, 0, index);
                postIds = trimmedIds;
            } else {
                postIds = new int[0];
                System.out.println("No posts found.");
            }

            postListView.setItems(myPosts);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deletePost() {
        if (selectedPostId == -1) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this post?",
                ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Confirm Delete");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try (Connection conn = DatabaseConnection.getConnection()) {
                    PreparedStatement stmt = conn.prepareStatement("DELETE FROM items WHERE item_id=?");
                    stmt.setInt(1, selectedPostId);
                    stmt.executeUpdate();
                    loadAllPosts();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void logout() {
        try {
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
