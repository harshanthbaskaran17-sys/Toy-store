package com.toystore.component4_admin.controller;

import com.toystore.common.SessionUtil;
import com.toystore.component1_user.service.UserService;
import com.toystore.component2_toy.service.ToyService;
import com.toystore.component3_order.service.OrderService;
import com.toystore.component4_admin.model.Admin;
import com.toystore.component4_admin.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// COMPONENT 04: Admin Management | Owner: Placeholder
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;
    private final ToyService toyService;
    private final OrderService orderService;
    private final SessionUtil sessionUtil;

    @Autowired
    public AdminController(AdminService adminService, UserService userService, 
                           ToyService toyService, OrderService orderService, SessionUtil sessionUtil) {
        this.adminService = adminService;
        this.userService = userService;
        this.toyService = toyService;
        this.orderService = orderService;
        this.sessionUtil = sessionUtil;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!sessionUtil.isAdmin(session)) return "redirect:/";
        
        model.addAttribute("userCount", userService.getAllUsers().size());
        model.addAttribute("toyCount", toyService.getAllToys().size());
        model.addAttribute("orderCount", orderService.getAllOrders().size());
        
        return "component4_admin/dashboard";
    }

    @GetMapping("/list")
    public String listAdmins(HttpSession session, Model model) {
        if (!sessionUtil.isAdmin(session)) return "redirect:/";
        
        model.addAttribute("admins", adminService.getAllAdmins());
        model.addAttribute("logs", adminService.getLogs());
        return "component4_admin/admin-panel";
    }

    @GetMapping("/register")
    public String showRegisterForm(HttpSession session) {
        if (!sessionUtil.isAdmin(session)) return "redirect:/";
        return "component4_admin/admin-register";
    }

    @PostMapping("/register")
    public String registerAdmin(@RequestParam String username, @RequestParam String password,
                                @RequestParam String email, @RequestParam String permissions,
                                HttpSession session, RedirectAttributes redirectAttributes) {
        if (!sessionUtil.isAdmin(session)) return "redirect:/";
        try {
            adminService.registerAdmin(username, password, email, permissions);
            adminService.logAction(sessionUtil.getLoggedInUserId(session), "Registered new admin: " + username);
            redirectAttributes.addFlashAttribute("successMessage", "Admin registered successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/register";
        }
        return "redirect:/admin/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable String id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!sessionUtil.isAdmin(session)) return "redirect:/";
        
        // Prevent deleting oneself
        if (sessionUtil.getLoggedInUserId(session).equals(id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "You cannot delete your own admin account.");
            return "redirect:/admin/list";
        }
        
        adminService.deleteAdmin(id);
        adminService.logAction(sessionUtil.getLoggedInUserId(session), "Deleted admin ID: " + id);
        redirectAttributes.addFlashAttribute("successMessage", "Admin deleted successfully.");
        return "redirect:/admin/list";
    }
}
