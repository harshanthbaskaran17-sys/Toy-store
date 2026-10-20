package com.toystore.component1_user.model;

// COMPONENT 01: User Management | Owner: Placeholder
public abstract class User {
    private String id;
    private String username;
    private String passwordHash;
    private String email;

    private String imageFilename;

    public User(String id, String username, String passwordHash, String email) {
        this(id, username, passwordHash, email, "");
    }

    public User(String id, String username, String passwordHash, String email, String imageFilename) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.imageFilename = imageFilename != null ? imageFilename : "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageFilename() {
        return imageFilename;
    }

    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename != null ? imageFilename : "";
    }

    public String getImageUrl() {
        if (imageFilename != null && !imageFilename.trim().isEmpty()) {
            return "/uploads/users/" + imageFilename;
        }
        return "/images/placeholder-user.svg";
    }

    public abstract String getRole();

    public String toFileString() {
        return id + "|" + username + "|" + passwordHash + "|" + email + "|" + getRole() + "|" + (imageFilename != null ? imageFilename : "");
    }
}
