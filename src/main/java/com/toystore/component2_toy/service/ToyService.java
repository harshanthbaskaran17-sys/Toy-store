package com.toystore.component2_toy.service;

import com.toystore.common.IdGenerator;
import com.toystore.common.ImageStorageUtil;
import com.toystore.component2_toy.model.EducationalToy;
import com.toystore.component2_toy.model.ElectronicToy;
import com.toystore.component2_toy.model.NonElectronicToy;
import com.toystore.component2_toy.model.PlushToy;
import com.toystore.component2_toy.model.Toy;
import com.toystore.component2_toy.repository.ToyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

// COMPONENT 02: Toy Management | Owner: Placeholder
@Service
public class ToyService {
    private final ToyRepository toyRepository;

    @Autowired
    public ToyService(ToyRepository toyRepository) {
        this.toyRepository = toyRepository;
    }

    public void addToy(String name, String brand, String type, String ageGroup, double price, int stockQuantity) {
        try {
            addToy(name, brand, type, ageGroup, price, stockQuantity, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addToy(String name, String brand, String type, String ageGroup, double price, int stockQuantity, MultipartFile imageFile) throws IOException {
        String id = IdGenerator.generateId("T");
        String imageFilename = "";
        if (imageFile != null && !imageFile.isEmpty()) {
            imageFilename = ImageStorageUtil.saveImage("toys", id, imageFile);
        }

        Toy toy;
        if ("EDUCATIONAL".equalsIgnoreCase(type)) {
            toy = new EducationalToy(id, name, brand, ageGroup, price, stockQuantity, imageFilename);
        } else if ("ELECTRONIC".equalsIgnoreCase(type)) {
            toy = new ElectronicToy(id, name, brand, ageGroup, price, stockQuantity, imageFilename);
        } else if ("NON_ELECTRONIC".equalsIgnoreCase(type) || "NON-ELECTRONIC".equalsIgnoreCase(type)) {
            toy = new NonElectronicToy(id, name, brand, ageGroup, price, stockQuantity, imageFilename);
        } else {
            toy = new PlushToy(id, name, brand, ageGroup, price, stockQuantity, imageFilename);
        }
        toyRepository.save(toy);
    }

    public List<Toy> getAllToys() {
        return toyRepository.findAll();
    }
    
    public Toy getToyById(String id) {
        return toyRepository.findById(id).orElse(null);
    }

    public List<Toy> searchToys(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllToys();
        }
        String lowerQuery = query.toLowerCase();
        return getAllToys().stream()
                .filter(t -> t.getName().toLowerCase().contains(lowerQuery) || 
                             t.getBrand().toLowerCase().contains(lowerQuery) ||
                             t.getCategory().toLowerCase().contains(lowerQuery) ||
                             t.getAgeGroup().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    public void updateToy(Toy toy) {
        toyRepository.update(toy);
    }

    public void updateToy(Toy toy, MultipartFile newImageFile) throws IOException {
        if (newImageFile != null && !newImageFile.isEmpty()) {
            // Delete old image if it exists
            if (toy.getImageFilename() != null && !toy.getImageFilename().isEmpty()) {
                ImageStorageUtil.deleteImage("toys", toy.getImageFilename());
            }
            String newFilename = ImageStorageUtil.saveImage("toys", toy.getId(), newImageFile);
            toy.setImageFilename(newFilename);
        }
        toyRepository.update(toy);
    }
    
    // Example of a method used by Order component
    public boolean checkAndReduceStock(String toyId, int quantity) {
        Toy toy = getToyById(toyId);
        if (toy != null && toy.getStockQuantity() >= quantity) {
            toy.setStockQuantity(toy.getStockQuantity() - quantity);
            updateToy(toy);
            return true;
        }
        return false;
    }
    
    public void increaseStock(String toyId, int quantity) {
        Toy toy = getToyById(toyId);
        if (toy != null) {
            toy.setStockQuantity(toy.getStockQuantity() + quantity);
            updateToy(toy);
        }
    }

    public void deleteToy(String id) {
        Toy toy = getToyById(id);
        if (toy != null) {
            if (toy.getImageFilename() != null && !toy.getImageFilename().isEmpty()) {
                ImageStorageUtil.deleteImage("toys", toy.getImageFilename());
            }
            toyRepository.delete(id);
        }
    }
}
