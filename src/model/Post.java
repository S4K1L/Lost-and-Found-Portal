package model;

public class Post {
    private String username;
    private String type;
    private String timeAgo;
    private String content;
    private String profileImageUrl;
    private String contactInfo;

    public Post(String username, String timeAgo, String content, String profileImageUrl, String contactInfo, String type) {
        this.username = username;
        this.type = type;
        this.timeAgo = timeAgo;
        this.content = content;
        this.profileImageUrl = profileImageUrl;
        this.contactInfo = contactInfo;
    }

    public String getUsername() { return username; }
    public String getTimeAgo() { return timeAgo; }
    public String getContent() { return content; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public String getContactInfo() { return contactInfo; }
    public String getType() { return type; }
}
