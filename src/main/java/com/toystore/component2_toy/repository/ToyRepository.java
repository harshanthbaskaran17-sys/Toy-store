package com.toystore.component2_toy.repository;

import com.toystore.common.FileHandler;
import com.toystore.component2_toy.model.EducationalToy;
import com.toystore.component2_toy.model.ElectronicToy;
import com.toystore.component2_toy.model.NonElectronicToy;
import com.toystore.component2_toy.model.PlushToy;
import com.toystore.component2_toy.model.Toy;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// COMPONENT 02: Toy Management | Owner: Placeholder
@Repository
public class ToyRepository {
    private final FileHandler fileHandler;

    public ToyRepository() {
        this.fileHandler = new FileHandler("toys.txt");
    }

    public synchronized void save(Toy toy) {
        fileHandler.appendLine(toy.toFileString());
    }

    public synchronized List<Toy> findAll() {
        List<String> lines = fileHandler.readAllLines();
        List<Toy> toys = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 8) {
                String id = parts[0];
                String name = parts[1];
                String brand = parts[2];
                // parts[3] is category, which is implicit in the type
                String ageGroup = parts[4];
                double price = Double.parseDouble(parts[5]);
                int stockQuantity = Integer.parseInt(parts[6]);
                String type = parts[7];
                String imageFilename = (parts.length >= 9) ? parts[8] : "";

                if ("EDUCATIONAL".equalsIgnoreCase(type)) {
                    toys.add(new EducationalToy(id, name, brand, ageGroup, price, stockQuantity, imageFilename));
                } else if ("ELECTRONIC".equalsIgnoreCase(type)) {
                    toys.add(new ElectronicToy(id, name, brand, ageGroup, price, stockQuantity, imageFilename));
                } else if ("PLUSH".equalsIgnoreCase(type)) {
                    toys.add(new PlushToy(id, name, brand, ageGroup, price, stockQuantity, imageFilename));
                } else if ("NON_ELECTRONIC".equalsIgnoreCase(type) || "NON-ELECTRONIC".equalsIgnoreCase(type)) {
                    toys.add(new NonElectronicToy(id, name, brand, ageGroup, price, stockQuantity, imageFilename));
                }
            }
        }
        return toys;
    }

    public Optional<Toy> findById(String id) {
        return findAll().stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    public synchronized void update(Toy updatedToy) {
        List<Toy> toys = findAll();
        for (int i = 0; i < toys.size(); i++) {
            if (toys.get(i).getId().equals(updatedToy.getId())) {
                toys.set(i, updatedToy);
                break;
            }
        }
        saveAll(toys);
    }

    public synchronized void delete(String id) {
        List<Toy> toys = findAll().stream()
                .filter(t -> !t.getId().equals(id))
                .collect(Collectors.toList());
        saveAll(toys);
    }

    private void saveAll(List<Toy> toys) {
        List<String> lines = toys.stream().map(Toy::toFileString).collect(Collectors.toList());
        fileHandler.writeAllLines(lines);
    }
}
