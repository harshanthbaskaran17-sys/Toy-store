package com.toystore.component4_admin.model;

// COMPONENT 04: Admin Management | Owner: Placeholder
public class ActivityLog {
    private String adminId;
    private String action;
    private String timestamp;

    public ActivityLog(String adminId, String action, String timestamp) {
        this.adminId = adminId;
        this.action = action;
        this.timestamp = timestamp;
    }

    public String getAdminId() { return adminId; }
    public String getAction() { return action; }
    public String getTimestamp() { return timestamp; }

    public String toFileString() {
        return adminId + "|" + action + "|" + timestamp;
    }
}
