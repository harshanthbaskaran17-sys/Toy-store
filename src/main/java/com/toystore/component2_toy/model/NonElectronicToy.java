package com.toystore.component2_toy.model;

// COMPONENT 02: Toy Management | Owner: Placeholder
public class NonElectronicToy extends Toy {

    public NonElectronicToy(String id, String name, String brand, String ageGroup, double price, int stockQuantity) {
        super(id, name, brand, "Non-Electronic", ageGroup, price, stockQuantity);
    }

    public NonElectronicToy(String id, String name, String brand, String ageGroup, double price, int stockQuantity, String imageFilename) {
        super(id, name, brand, "Non-Electronic", ageGroup, price, stockQuantity, imageFilename);
    }

    @Override
    public String displayDetails() {
        return "Non-Electronic Toy: " + getName() + " by " + getBrand() + ". Classic screen-free tactile play.";
    }

    @Override
    public String getSafetyInfo() {
        return "Non-toxic materials. Safe for hands-on, active creative play.";
    }

    @Override
    public String getToyType() {
        return "NON_ELECTRONIC";
    }
}
