package com.toystore.component4_admin.model;

// COMPONENT 04: Admin Management | Owner: Placeholder
// Abstraction
public interface AdminOperations {
    void registerAdmin(String username, String password, String email, String permissions) throws Exception;
    void logAction(String adminId, String action);
}
