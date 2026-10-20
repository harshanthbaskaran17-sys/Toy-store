package com.toystore.component4_admin.service;

import com.toystore.common.IdGenerator;
import com.toystore.component4_admin.model.ActivityLog;
import com.toystore.component4_admin.model.Admin;
import com.toystore.component4_admin.model.AdminOperations;
import com.toystore.component4_admin.repository.ActivityLogRepository;
import com.toystore.component4_admin.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

// COMPONENT 04: Admin Management | Owner: Placeholder
@Service
public class AdminService implements AdminOperations {
    private final AdminRepository adminRepository;
    private final ActivityLogRepository logRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository, ActivityLogRepository logRepository) {
        this.adminRepository = adminRepository;
        this.logRepository = logRepository;
    }

    @Override
    public void registerAdmin(String username, String password, String email, String permissions) throws Exception {
        if (adminRepository.findByUsername(username).isPresent()) {
            throw new Exception("Admin username already exists");
        }
        String id = IdGenerator.generateId("A");
        String hashedPassword = hashPassword(password);
        Admin admin = new Admin(id, username, hashedPassword, email, permissions);
        adminRepository.save(admin);
    }

    @Override
    public void logAction(String adminId, String action) {
        String timestamp = LocalDateTime.now().toString();
        logRepository.save(new ActivityLog(adminId, action, timestamp));
    }
    
    public Admin login(String username, String password) throws Exception {
        Optional<Admin> adminOpt = adminRepository.findByUsername(username);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            if (admin.getPasswordHash().equals(hashPassword(password))) {
                return admin;
            }
        }
        throw new Exception("Invalid admin username or password");
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }
    
    public Admin getAdminById(String id) {
        return adminRepository.findById(id).orElse(null);
    }
    
    public void updateAdmin(Admin admin) {
        adminRepository.update(admin);
    }
    
    public void deleteAdmin(String id) {
        adminRepository.delete(id);
    }
    
    public List<ActivityLog> getLogs() {
        return logRepository.findAll();
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
