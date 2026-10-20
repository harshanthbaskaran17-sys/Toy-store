package com.toystore.component5_supplier.controller;

import com.toystore.common.SessionUtil;
import com.toystore.component5_supplier.model.Supplier;
import com.toystore.component5_supplier.service.SupplierService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

// COMPONENT 05: Supplier / Brand Management | Owner: Placeholder
@Controller
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierService supplierService;
    private final SessionUtil sessionUtil;

    @Autowired
    public SupplierController(SupplierService supplierService, SessionUtil sessionUtil) {
        this.supplierService = supplierService;
        this.sessionUtil = sessionUtil;
    }

    @GetMapping
    public String listSuppliers(@RequestParam(required = false) String search, HttpSession session, Model model) {
        if (!sessionUtil.isAdmin(session)) return "redirect:/";
        
        List<Supplier> suppliers;
        if (search != null && !search.isEmpty()) {
            suppliers = supplierService.searchSuppliers(search);
            model.addAttribute("searchQuery", search);
        } else {
            suppliers = supplierService.getAllSuppliers();
        }
        model.addAttribute("suppliers", suppliers);
        return "component5_supplier/supplier-list";
    }

    @GetMapping("/register")
    public String showRegisterForm(HttpSession session) {
        if (!sessionUtil.isAdmin(session)) return "redirect:/";
        return "component5_supplier/supplier-register";
    }

    @PostMapping("/register")
    public String registerSupplier(@RequestParam String name, @RequestParam String contact,
                                   @RequestParam String brands, @RequestParam String type,
                                   @RequestParam(value = "image", required = false) org.springframework.web.multipart.MultipartFile image,
                                   HttpSession session, RedirectAttributes redirectAttributes) {
        if (!sessionUtil.isAdmin(session)) return "redirect:/";
        try {
            supplierService.addSupplier(name, contact, brands, type, image);
            redirectAttributes.addFlashAttribute("successMessage", "Supplier added successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/suppliers/register";
        }
        return "redirect:/suppliers";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, HttpSession session, Model model) {
        if (!sessionUtil.isAdmin(session)) return "redirect:/";
        Supplier supplier = supplierService.getSupplierById(id);
        if (supplier == null) return "redirect:/suppliers";
        model.addAttribute("supplier", supplier);
        return "component5_supplier/supplier-edit";
    }

    @PostMapping("/edit/{id}")
    public String editSupplier(@PathVariable String id, @RequestParam String contact,
                               @RequestParam String brands,
                               @RequestParam(value = "image", required = false) org.springframework.web.multipart.MultipartFile image,
                               HttpSession session, 
                               RedirectAttributes redirectAttributes) {
        if (!sessionUtil.isAdmin(session)) return "redirect:/";
        try {
            Supplier supplier = supplierService.getSupplierById(id);
            if (supplier != null) {
                supplier.setContact(contact);
                supplier.setBrandsSupplied(brands);
                supplierService.updateSupplier(supplier, image);
                redirectAttributes.addFlashAttribute("successMessage", "Supplier updated successfully.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/suppliers/edit/" + id;
        }
        return "redirect:/suppliers";
    }

    @PostMapping("/delete/{id}")
    public String deleteSupplier(@PathVariable String id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!sessionUtil.isAdmin(session)) return "redirect:/";
        supplierService.deleteSupplier(id);
        redirectAttributes.addFlashAttribute("successMessage", "Supplier deleted successfully.");
        return "redirect:/suppliers";
    }
}
