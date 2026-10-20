package com.toystore.component1_user.service;

import com.toystore.common.IdGenerator;
import com.toystore.component1_user.model.RegularUser;
import com.toystore.component1_user.model.User;
import com.toystore.component1_user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

// COMPONENT 01: User Management | Owner: Placeholder
@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerRegularUser(String username, String password, String email, String membershipType) throws Exception {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new Exception("Username already exists");
        }
        String id = IdGenerator.generateId("U");
        String hashedPassword = hashPassword(password);
        RegularUser user = new RegularUser(id, username, hashedPassword, email, membershipType);
        userRepository.save(user);
    }

    public User login(String username, String password) throws Exception {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPasswordHash().equals(hashPassword(password))) {
                return user;
            }
        }
        throw new Exception("Invalid username or password");
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User getUserById(String id) {
         return userRepository.findById(id).orElse(null);
    }
    
    public void updateUser(User user) {
        userRepository.update(user);
    }

    public void updateUserProfile(String userId, String email, String membershipType, org.springframework.web.multipart.MultipartFile imageFile) throws Exception {
        User user = getUserById(userId);
        if (user != null) {
            user.setEmail(email);
            if (user instanceof RegularUser && membershipType != null) {
                ((RegularUser) user).setMembershipType(membershipType);
            }
            if (imageFile != null && !imageFile.isEmpty()) {
                if (user.getImageFilename() != null && !user.getImageFilename().isEmpty()) {
                    com.toystore.common.ImageStorageUtil.deleteImage("users", user.getImageFilename());
                }
                String newFilename = com.toystore.common.ImageStorageUtil.saveImage("users", user.getId(), imageFile);
                user.setImageFilename(newFilename);
            }
            updateUser(user);
        }
    }
    
    public void deleteUser(String id) {
        User user = getUserById(id);
        if (user != null) {
            if (user.getImageFilename() != null && !user.getImageFilename().isEmpty()) {
                com.toystore.common.ImageStorageUtil.deleteImage("users", user.getImageFilename());
            }
            userRepository.delete(id);
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
