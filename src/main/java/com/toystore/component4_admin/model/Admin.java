package com.toystore.component4_admin.model;

import com.toystore.component1_user.model.User;

// COMPONENT 04: Admin Management | Owner: Placeholder
// Encapsulation and Inheritance
public class Admin extends User {
    private String permissions; // e.g. "ALL", "READ_ONLY"

    public Admin(String id, String username, String passwordHash, String email, String permissions) {
        super(id, username, passwordHash, email);
        this.permissions = permissions;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }
    
    @Override
    public String toFileString() {
        return getId() + "|" + getUsername() + "|" + getPasswordHash() + "|" + getEmail() + "|" + getRole() + "|" + permissions;
    }
}
