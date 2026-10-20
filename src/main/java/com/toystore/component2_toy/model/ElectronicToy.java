package com.toystore.component2_toy.model;

// COMPONENT 02: Toy Management | Owner: Placeholder
public class ElectronicToy extends Toy {

    public ElectronicToy(String id, String name, String brand, String ageGroup, double price, int stockQuantity) {
        super(id, name, brand, "Electronic", ageGroup, price, stockQuantity);
    }

    public ElectronicToy(String id, String name, String brand, String ageGroup, double price, int stockQuantity, String imageFilename) {
        super(id, name, brand, "Electronic", ageGroup, price, stockQuantity, imageFilename);
    }

    @Override
    public String displayDetails() {
        return "Electronic Toy: " + getName() + " by " + getBrand() + ". Requires batteries.";
    }

    @Override
    public String getSafetyInfo() {
        return "Caution: Contains button or coin cell battery. Hazardous if swallowed.";
    }

    @Override
    public String getToyType() {
        return "ELECTRONIC";
    }
}
