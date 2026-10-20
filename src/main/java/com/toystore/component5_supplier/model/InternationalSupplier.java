package com.toystore.component5_supplier.model;

// COMPONENT 05: Supplier / Brand Management | Owner: Placeholder
public class InternationalSupplier extends Supplier {

    public InternationalSupplier(String id, String name, String contact, String brandsSupplied) {
        super(id, name, contact, brandsSupplied);
    }

    public InternationalSupplier(String id, String name, String contact, String brandsSupplied, String imageFilename) {
        super(id, name, contact, brandsSupplied, imageFilename);
    }

    @Override
    public String getDeliveryTimeEstimate() {
        return "10-15 Business Days (Customs dependent)";
    }

    @Override
    public double getRestockCost() {
        return 20000.0; // Higher cost for international
    }

    @Override
    public String getType() {
        return "INTERNATIONAL";
    }
}
