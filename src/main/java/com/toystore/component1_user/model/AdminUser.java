package com.toystore.component1_user.model;

// COMPONENT 01: User Management | Owner: Placeholder
public class AdminUser extends User {

    public AdminUser(String id, String username, String passwordHash, String email) {
        super(id, username, passwordHash, email);
    }

    public AdminUser(String id, String username, String passwordHash, String email, String imageFilename) {
        super(id, username, passwordHash, email, imageFilename);
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }
}
