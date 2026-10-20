package com.toystore.component2_toy.model;

// COMPONENT 02: Toy Management | Owner: Placeholder
public class PlushToy extends Toy {

    public PlushToy(String id, String name, String brand, String ageGroup, double price, int stockQuantity) {
        super(id, name, brand, "Plush", ageGroup, price, stockQuantity);
    }

    public PlushToy(String id, String name, String brand, String ageGroup, double price, int stockQuantity, String imageFilename) {
        super(id, name, brand, "Plush", ageGroup, price, stockQuantity, imageFilename);
    }

    @Override
    public String displayDetails() {
        return "Plush Toy: " + getName() + " by " + getBrand() + ". Soft and cuddly.";
    }

    @Override
    public String getSafetyInfo() {
        return "Safety: Keep away from fire. Remove all tags before giving to a child.";
    }

    @Override
    public String getToyType() {
        return "PLUSH";
    }
}
