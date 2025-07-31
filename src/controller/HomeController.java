package controller;

import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.Post;

import java.sql.*;

public class HomeController {
    @FXML private ListView<Post> postListView;
    @FXML private Button myPostsButton;
    @FXML private Button profileButton;
    @FXML private Button logoutButton;

    @FXML
    private void initialize() {
        loadPosts();

        // ✅ Set custom cell factory for social media-style posts
        postListView.setCellFactory(listView -> new PostCell());

        myPostsButton.setOnAction(e -> switchScene("/view/MyPosts.fxml"));
        profileButton.setOnAction(e -> switchScene("/view/Profile.fxml"));
        logoutButton.setOnAction(e -> switchScene("/view/Login.fxml"));
    }

    private void loadPosts() {
    ObservableList<Post> posts = FXCollections.observableArrayList();

    try (Connection conn = DatabaseConnection.getConnection()) {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM items ORDER BY date_reported DESC");

        while (rs.next()) {
            Post post = new Post(
                    rs.getString("item_name"),                 // Username (item reporter name or title)
                    rs.getDate("date_reported").toString(),    // Time
                    rs.getString("description"),               // Content
                    "/images/profile.png",             // Profile image (static placeholder)
                    rs.getString("contact_info"),               // ✅ Contact Info
                    rs.getString("type")
            );
            posts.add(post);
        }

        postListView.setItems(posts);

    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    private void switchScene(String fxmlPath) {
        try {
            Stage stage = (Stage) postListView.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
