package com.toystore.component5_supplier.service;

import com.toystore.common.IdGenerator;
import com.toystore.component5_supplier.model.InternationalSupplier;
import com.toystore.component5_supplier.model.LocalSupplier;
import com.toystore.component5_supplier.model.Supplier;
import com.toystore.component5_supplier.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// COMPONENT 05: Supplier / Brand Management | Owner: Placeholder
@Service
public class SupplierService {
    private final SupplierRepository supplierRepository;

    @Autowired
    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public void addSupplier(String name, String contact, String brands, String type) {
        try {
            addSupplier(name, contact, brands, type, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addSupplier(String name, String contact, String brands, String type, org.springframework.web.multipart.MultipartFile imageFile) throws java.io.IOException {
        String id = IdGenerator.generateId("S");
        String imageFilename = "";
        if (imageFile != null && !imageFile.isEmpty()) {
            imageFilename = com.toystore.common.ImageStorageUtil.saveImage("suppliers", id, imageFile);
        }

        Supplier supplier;
        if ("LOCAL".equals(type)) {
            supplier = new LocalSupplier(id, name, contact, brands, imageFilename);
        } else {
            supplier = new InternationalSupplier(id, name, contact, brands, imageFilename);
        }
        supplierRepository.save(supplier);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }
    
    public Supplier getSupplierById(String id) {
        return supplierRepository.findById(id).orElse(null);
    }

    public List<Supplier> searchSuppliers(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllSuppliers();
        }
        String lowerQuery = query.toLowerCase();
        return getAllSuppliers().stream()
                .filter(s -> s.getName().toLowerCase().contains(lowerQuery) || 
                             s.getBrandsSupplied().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    public void updateSupplier(Supplier supplier) {
        supplierRepository.update(supplier);
    }

    public void updateSupplier(Supplier supplier, org.springframework.web.multipart.MultipartFile newImageFile) throws java.io.IOException {
        if (newImageFile != null && !newImageFile.isEmpty()) {
            if (supplier.getImageFilename() != null && !supplier.getImageFilename().isEmpty()) {
                com.toystore.common.ImageStorageUtil.deleteImage("suppliers", supplier.getImageFilename());
            }
            String newFilename = com.toystore.common.ImageStorageUtil.saveImage("suppliers", supplier.getId(), newImageFile);
            supplier.setImageFilename(newFilename);
        }
        supplierRepository.update(supplier);
    }

    public void deleteSupplier(String id) {
        Supplier supplier = getSupplierById(id);
        if (supplier != null) {
            if (supplier.getImageFilename() != null && !supplier.getImageFilename().isEmpty()) {
                com.toystore.common.ImageStorageUtil.deleteImage("suppliers", supplier.getImageFilename());
            }
            supplierRepository.delete(id);
        }
    }
}
