package com.toystore.component2_toy.model;

// COMPONENT 02: Toy Management | Owner: Placeholder
public class EducationalToy extends Toy {

    public EducationalToy(String id, String name, String brand, String ageGroup, double price, int stockQuantity) {
        super(id, name, brand, "Educational", ageGroup, price, stockQuantity);
    }

    public EducationalToy(String id, String name, String brand, String ageGroup, double price, int stockQuantity, String imageFilename) {
        super(id, name, brand, "Educational", ageGroup, price, stockQuantity, imageFilename);
    }

    @Override
    public String displayDetails() {
        return "Educational Toy: " + getName() + " by " + getBrand() + ". Focuses on learning and development.";
    }

    @Override
    public String getSafetyInfo() {
        return "Warning: Choking hazard - small parts. Not for children under 3 years.";
    }

    @Override
    public String getToyType() {
        return "EDUCATIONAL";
    }
}
