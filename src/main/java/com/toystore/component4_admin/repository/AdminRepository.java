package com.toystore.component4_admin.repository;

import com.toystore.common.FileHandler;
import com.toystore.component4_admin.model.Admin;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// COMPONENT 04: Admin Management | Owner: Placeholder
@Repository
public class AdminRepository {
    private final FileHandler fileHandler;

    public AdminRepository() {
        this.fileHandler = new FileHandler("admins.txt");
    }

    public synchronized void save(Admin admin) {
        fileHandler.appendLine(admin.toFileString());
    }

    public synchronized List<Admin> findAll() {
        List<String> lines = fileHandler.readAllLines();
        List<Admin> admins = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 6) {
                admins.add(new Admin(parts[0], parts[1], parts[2], parts[3], parts[5]));
            }
        }
        return admins;
    }

    public Optional<Admin> findByUsername(String username) {
        return findAll().stream()
                .filter(a -> a.getUsername().equals(username))
                .findFirst();
    }
    
    public Optional<Admin> findById(String id) {
        return findAll().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    public synchronized void update(Admin updatedAdmin) {
        List<Admin> admins = findAll();
        for (int i = 0; i < admins.size(); i++) {
            if (admins.get(i).getId().equals(updatedAdmin.getId())) {
                admins.set(i, updatedAdmin);
                break;
            }
        }
        saveAll(admins);
    }
    
    public synchronized void delete(String id) {
        List<Admin> admins = findAll().stream()
                .filter(a -> !a.getId().equals(id))
                .collect(Collectors.toList());
        saveAll(admins);
    }

    private void saveAll(List<Admin> admins) {
        List<String> lines = admins.stream().map(Admin::toFileString).collect(Collectors.toList());
        fileHandler.writeAllLines(lines);
    }
}
