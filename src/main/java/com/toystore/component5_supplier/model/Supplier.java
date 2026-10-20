package com.toystore.component5_supplier.model;

// COMPONENT 05: Supplier / Brand Management | Owner: Placeholder
// Encapsulation and Polymorphism
public abstract class Supplier {
    private String id;
    private String name;
    private String contact;
    private String brandsSupplied;

    private String imageFilename;

    public Supplier(String id, String name, String contact, String brandsSupplied) {
        this(id, name, contact, brandsSupplied, "");
    }

    public Supplier(String id, String name, String contact, String brandsSupplied, String imageFilename) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.brandsSupplied = brandsSupplied;
        this.imageFilename = imageFilename != null ? imageFilename : "";
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getBrandsSupplied() { return brandsSupplied; }
    public void setBrandsSupplied(String brandsSupplied) { this.brandsSupplied = brandsSupplied; }

    public String getImageFilename() { return imageFilename; }
    public void setImageFilename(String imageFilename) { this.imageFilename = imageFilename != null ? imageFilename : ""; }

    public String getImageUrl() {
        if (imageFilename != null && !imageFilename.trim().isEmpty()) {
            return "/uploads/suppliers/" + imageFilename;
        }
        return "/images/placeholder-supplier.svg";
    }

    public abstract String getDeliveryTimeEstimate();
    public abstract double getRestockCost();
    
    public abstract String getType();

    public String toFileString() {
        return id + "|" + name + "|" + contact + "|" + brandsSupplied + "|" + getType() + "|" + (imageFilename != null ? imageFilename : "");
    }
}
