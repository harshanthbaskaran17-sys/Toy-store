package com.toystore.component5_supplier.repository;

import com.toystore.common.FileHandler;
import com.toystore.component5_supplier.model.InternationalSupplier;
import com.toystore.component5_supplier.model.LocalSupplier;
import com.toystore.component5_supplier.model.Supplier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// COMPONENT 05: Supplier / Brand Management | Owner: Placeholder
@Repository
public class SupplierRepository {
    private final FileHandler fileHandler;

    public SupplierRepository() {
        this.fileHandler = new FileHandler("suppliers.txt");
    }

    public synchronized void save(Supplier supplier) {
        fileHandler.appendLine(supplier.toFileString());
    }

    public synchronized List<Supplier> findAll() {
        List<String> lines = fileHandler.readAllLines();
        List<Supplier> suppliers = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 5) {
                String imageFilename = (parts.length >= 6) ? parts[5] : "";
                if ("LOCAL".equals(parts[4])) {
                    suppliers.add(new LocalSupplier(parts[0], parts[1], parts[2], parts[3], imageFilename));
                } else {
                    suppliers.add(new InternationalSupplier(parts[0], parts[1], parts[2], parts[3], imageFilename));
                }
            }
        }
        return suppliers;
    }

    public Optional<Supplier> findById(String id) {
        return findAll().stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }

    public synchronized void update(Supplier updatedSupplier) {
        List<Supplier> suppliers = findAll();
        for (int i = 0; i < suppliers.size(); i++) {
            if (suppliers.get(i).getId().equals(updatedSupplier.getId())) {
                suppliers.set(i, updatedSupplier);
                break;
            }
        }
        saveAll(suppliers);
    }

    public synchronized void delete(String id) {
        List<Supplier> suppliers = findAll().stream()
                .filter(s -> !s.getId().equals(id))
                .collect(Collectors.toList());
        saveAll(suppliers);
    }

    private void saveAll(List<Supplier> suppliers) {
        List<String> lines = suppliers.stream().map(Supplier::toFileString).collect(Collectors.toList());
        fileHandler.writeAllLines(lines);
    }
}
