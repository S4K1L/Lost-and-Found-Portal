package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import model.Post;
import java.io.IOException;

public class PostCell extends ListCell<Post> {

    @FXML private HBox root;
    @FXML private ImageView profileImage;
    @FXML private Label usernameLabel;
    @FXML private Label timeLabel;
    @FXML private Label contentLabel;
    @FXML private Label contactLabel;
    @FXML private Label type;

    private FXMLLoader loader;

    @Override
    protected void updateItem(Post post, boolean empty) {
        super.updateItem(post, empty);

        if (empty || post == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("/view/post_cell.fxml"));
                loader.setController(this);
                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            type.setText(post.getType() != null ? post.getType() : "Unknown");
            usernameLabel.setText(post.getUsername() != null ? post.getUsername() : "Unknown User");
            timeLabel.setText(post.getTimeAgo() != null ? post.getTimeAgo() : "");
            contentLabel.setText(post.getContent() != null ? post.getContent() : "");
            contactLabel.setText(post.getContactInfo() != null ? "📞 Contact: " + post.getContactInfo() : "No Contact Info");

            if (post.getProfileImageUrl() != null && !post.getProfileImageUrl().isEmpty()) {
                profileImage.setImage(new Image(post.getProfileImageUrl(), true));
            } else {
                profileImage.setImage(new Image(getClass().getResource("/images/profile.png").toExternalForm())); 
            }

            setGraphic(root);
        }
    }
}
