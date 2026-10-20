package com.toystore.component1_user.controller;

import com.toystore.common.SessionUtil;
import com.toystore.component1_user.model.RegularUser;
import com.toystore.component1_user.model.User;
import com.toystore.component1_user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

// COMPONENT 01: User Management | Owner: Placeholder
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final com.toystore.component4_admin.service.AdminService adminService;
    private final SessionUtil sessionUtil;

    @Autowired
    public UserController(UserService userService, com.toystore.component4_admin.service.AdminService adminService, SessionUtil sessionUtil) {
        this.userService = userService;
        this.adminService = adminService;
        this.sessionUtil = sessionUtil;
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "component1_user/register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password,
                               @RequestParam String email, @RequestParam String membershipType,
                               RedirectAttributes redirectAttributes) {
        try {
            userService.registerRegularUser(username, password, email, membershipType);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful. Please login.");
            return "redirect:/users/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users/register";
        }
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "component1_user/login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password,
                            HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.login(username, password);
            sessionUtil.loginUser(session, user.getId(), user.getUsername(), user.getRole());
            redirectAttributes.addFlashAttribute("successMessage", "Welcome back, " + user.getUsername() + "!");
            return "redirect:/";
        } catch (Exception e) {
            // If regular login fails, try admin login
            try {
                com.toystore.component4_admin.model.Admin admin = adminService.login(username, password);
                sessionUtil.loginUser(session, admin.getId(), admin.getUsername(), admin.getRole());
                redirectAttributes.addFlashAttribute("successMessage", "Welcome back, Admin " + admin.getUsername() + "!");
                adminService.logAction(admin.getId(), "Logged in");
                return "redirect:/admin/dashboard";
            } catch (Exception ex) {
                redirectAttributes.addFlashAttribute("errorMessage", "Invalid username or password");
                return "redirect:/users/login";
            }
        }
    }

    @GetMapping("/logout")
    public String logoutUser(HttpSession session, RedirectAttributes redirectAttributes) {
        sessionUtil.logoutUser(session);
        redirectAttributes.addFlashAttribute("successMessage", "Logged out successfully.");
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        if (!sessionUtil.isLoggedIn(session)) return "redirect:/users/login";
        User user = userService.getUserById(sessionUtil.getLoggedInUserId(session));
        model.addAttribute("user", user);
        return "component1_user/profile-update";
    }
    
    @PostMapping("/profile")
    public String updateProfile(@RequestParam String email, 
                                @RequestParam(required = false) String membershipType,
                                @RequestParam(value = "image", required = false) org.springframework.web.multipart.MultipartFile image,
                                HttpSession session, RedirectAttributes redirectAttributes) {
        if (!sessionUtil.isLoggedIn(session)) return "redirect:/users/login";
        
        try {
            String userId = sessionUtil.getLoggedInUserId(session);
            userService.updateUserProfile(userId, email, membershipType, image);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/users/profile";
    }

    @GetMapping
    public String listUsers(HttpSession session, Model model) {
        if (!sessionUtil.isAdmin(session)) return "redirect:/";
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "component1_user/user-list";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable String id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!sessionUtil.isAdmin(session)) return "redirect:/";
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully.");
        return "redirect:/users";
    }
}
