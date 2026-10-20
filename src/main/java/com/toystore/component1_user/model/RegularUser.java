package com.toystore.component1_user.model;

// COMPONENT 01: User Management | Owner: Placeholder
public class RegularUser extends User {
    private String membershipType; // REGULAR or PREMIUM

    public RegularUser(String id, String username, String passwordHash, String email, String membershipType) {
        super(id, username, passwordHash, email);
        this.membershipType = membershipType;
    }

    public RegularUser(String id, String username, String passwordHash, String email, String membershipType, String imageFilename) {
        super(id, username, passwordHash, email, imageFilename);
        this.membershipType = membershipType;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    @Override
    public String getRole() {
        return membershipType; // e.g., REGULAR, PREMIUM
    }
}
