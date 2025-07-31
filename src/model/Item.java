package model;

import java.sql.Date;

public class Item {
    private int id;
    private String type, name, description, location, status, contact, imagePath, userMail;
    private Date dateReported;

    public Item() {}

    public Item(int id, String type, String name, String description, String location, Date dateReported, String status, String contact, String imagePath, String userMail) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.userMail = userMail;
        this.description = description;
        this.location = location;
        this.dateReported = dateReported;
        this.status = status;
        this.contact = contact;
        this.imagePath = imagePath;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public void setUserMail(String userMail) { this.userMail = userMail; }
    public String getUserMail() { return userMail; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Date getDateReported() { return dateReported; }
    public void setDateReported(Date dateReported) { this.dateReported = dateReported; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}