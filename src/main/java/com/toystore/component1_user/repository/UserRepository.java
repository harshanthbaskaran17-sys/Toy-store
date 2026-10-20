package com.toystore.component1_user.repository;

import com.toystore.common.FileHandler;
import com.toystore.component1_user.model.AdminUser;
import com.toystore.component1_user.model.RegularUser;
import com.toystore.component1_user.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// COMPONENT 01: User Management | Owner: Placeholder
@Repository
public class UserRepository {
    private final FileHandler fileHandler;

    public UserRepository() {
        this.fileHandler = new FileHandler("users.txt");
    }

    public synchronized void save(User user) {
        fileHandler.appendLine(user.toFileString());
    }

    public synchronized List<User> findAll() {
        List<String> lines = fileHandler.readAllLines();
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 5) {
                String imageFilename = (parts.length >= 6) ? parts[5] : "";
                if ("ADMIN".equals(parts[4])) {
                    users.add(new AdminUser(parts[0], parts[1], parts[2], parts[3], imageFilename));
                } else {
                    users.add(new RegularUser(parts[0], parts[1], parts[2], parts[3], parts[4], imageFilename));
                }
            }
        }
        return users;
    }

    public Optional<User> findByUsername(String username) {
        return findAll().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    public Optional<User> findById(String id) {
        return findAll().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    public synchronized void update(User updatedUser) {
        List<User> users = findAll();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(updatedUser.getId())) {
                users.set(i, updatedUser);
                break;
            }
        }
        saveAll(users);
    }

    public synchronized void delete(String id) {
        List<User> users = findAll().stream()
                .filter(u -> !u.getId().equals(id))
                .collect(Collectors.toList());
        saveAll(users);
    }

    private void saveAll(List<User> users) {
        List<String> lines = users.stream().map(User::toFileString).collect(Collectors.toList());
        fileHandler.writeAllLines(lines);
    }
}
