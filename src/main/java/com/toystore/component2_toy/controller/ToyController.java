package com.toystore.component2_toy.controller;

import com.toystore.common.SessionUtil;
import com.toystore.component2_toy.model.Toy;
import com.toystore.component2_toy.service.ToyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

// COMPONENT 02: Toy Management | Owner: Placeholder
@Controller
@RequestMapping("/toys")
public class ToyController {

    private final ToyService toyService;
    private final SessionUtil sessionUtil;

    @Autowired
    public ToyController(ToyService toyService, SessionUtil sessionUtil) {
        this.toyService = toyService;
        this.sessionUtil = sessionUtil;
    }

    @GetMapping
    public String listToys(@RequestParam(required = false) String search, Model model) {
        List<Toy> toys;
        if (search != null && !search.isEmpty()) {
            toys = toyService.searchToys(search);
            model.addAttribute("searchQuery", search);
        } else {
            toys = toyService.getAllToys();
        }
        model.addAttribute("toys", toys);
        return "component2_toy/toy-list";
    }
    
    @GetMapping("/search")
    public String searchPage() {
        return "component2_toy/toy-search";
    }

    @GetMapping("/{id}")
    public String viewToyDetails(@PathVariable String id, Model model) {
        Toy toy = toyService.getToyById(id);
        if (toy == null) {
            return "redirect:/toys";
        }
        model.addAttribute("toy", toy);
        return "component2_toy/toy-details";
    }

    @GetMapping("/add")
    public String showAddToyForm(HttpSession session) {
        if (!sessionUtil.isAdmin(session)) return "redirect:/";
        return "component2_toy/toy-add";
    }

    @PostMapping("/add")
    public String addToy(@RequestParam String name, @RequestParam String brand,
                         @RequestParam String type, @RequestParam String ageGroup,
                         @RequestParam double price, @RequestParam int stockQuantity,
                         @RequestParam(value = "image", required = false) MultipartFile image,
                         HttpSession session, RedirectAttributes redirectAttributes) {
        if (!sessionUtil.isAdmin(session)) return "redirect:/";
        
        try {
            if(price < 0 || stockQuantity < 0) {
                 throw new IllegalArgumentException("Price and stock must be positive.");
            }
            toyService.addToy(name, brand, type, ageGroup, price, stockQuantity, image);
            redirectAttributes.addFlashAttribute("successMessage", "Toy added successfully.");
        } catch(Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/toys/add";
        }
        return "redirect:/toys";
    }

    @GetMapping("/edit/{id}")
    public String showEditToyForm(@PathVariable String id, HttpSession session, Model model) {
        if (!sessionUtil.isAdmin(session)) return "redirect:/";
        Toy toy = toyService.getToyById(id);
        if (toy == null) return "redirect:/toys";
        model.addAttribute("toy", toy);
        return "component2_toy/toy-edit";
    }

    @PostMapping("/edit/{id}")
    public String editToy(@PathVariable String id, @RequestParam double price, 
                          @RequestParam int stockQuantity,
                          @RequestParam(value = "image", required = false) MultipartFile image,
                          HttpSession session, 
                          RedirectAttributes redirectAttributes) {
        if (!sessionUtil.isAdmin(session)) return "redirect:/";
        try {
            if(price < 0 || stockQuantity < 0) {
                 throw new IllegalArgumentException("Price and stock must be positive.");
            }
            Toy toy = toyService.getToyById(id);
            if (toy != null) {
                toy.setPrice(price);
                toy.setStockQuantity(stockQuantity);
                toyService.updateToy(toy, image);
                redirectAttributes.addFlashAttribute("successMessage", "Toy updated successfully.");
            }
        } catch(Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/toys/edit/" + id;
        }
        return "redirect:/toys";
    }

    @PostMapping("/delete/{id}")
    public String deleteToy(@PathVariable String id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!sessionUtil.isAdmin(session)) return "redirect:/";
        toyService.deleteToy(id);
        redirectAttributes.addFlashAttribute("successMessage", "Toy deleted successfully.");
        return "redirect:/toys";
    }
}
