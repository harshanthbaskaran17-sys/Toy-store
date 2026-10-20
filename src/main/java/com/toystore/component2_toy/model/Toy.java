package com.toystore.component2_toy.model;

// COMPONENT 02: Toy Management | Owner: Placeholder
public abstract class Toy {
    private String id;
    private String name;
    private String brand;
    private String category;
    private String ageGroup;
    private double price;
    private int stockQuantity;
    private String imageFilename;

    public Toy(String id, String name, String brand, String category, String ageGroup, double price, int stockQuantity) {
        this(id, name, brand, category, ageGroup, price, stockQuantity, "");
    }

    public Toy(String id, String name, String brand, String category, String ageGroup, double price, int stockQuantity, String imageFilename) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.ageGroup = ageGroup;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.imageFilename = imageFilename != null ? imageFilename : "";
    }

    // Getters and Setters (Encapsulation)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getAgeGroup() { return ageGroup; }
    public void setAgeGroup(String ageGroup) { this.ageGroup = ageGroup; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public String getImageFilename() { return imageFilename; }
    public void setImageFilename(String imageFilename) { this.imageFilename = imageFilename != null ? imageFilename : ""; }

    public String getImageUrl() {
        if (imageFilename != null && !imageFilename.trim().isEmpty()) {
            return "/uploads/toys/" + imageFilename;
        }
        return "/images/placeholder-toy.svg";
    }

    // Abstract methods for Polymorphism
    public abstract String displayDetails();
    public abstract String getSafetyInfo();

    public String toFileString() {
        return id + "|" + name + "|" + brand + "|" + category + "|" + ageGroup + "|" + price + "|" + stockQuantity + "|" + getToyType() + "|" + (imageFilename != null ? imageFilename : "");
    }
    
    public abstract String getToyType();
}
