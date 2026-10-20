package com.toystore.component5_supplier.model;

// COMPONENT 05: Supplier / Brand Management | Owner: Placeholder
public class LocalSupplier extends Supplier {

    public LocalSupplier(String id, String name, String contact, String brandsSupplied) {
        super(id, name, contact, brandsSupplied);
    }

    public LocalSupplier(String id, String name, String contact, String brandsSupplied, String imageFilename) {
        super(id, name, contact, brandsSupplied, imageFilename);
    }

    @Override
    public String getDeliveryTimeEstimate() {
        return "1-3 Business Days";
    }

    @Override
    public double getRestockCost() {
        return 5000.0; // Fixed flat rate for local
    }

    @Override
    public String getType() {
        return "LOCAL";
    }
}
