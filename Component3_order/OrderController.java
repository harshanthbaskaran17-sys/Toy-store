package com.toystore.component3_order.controller;

import com.toystore.common.SessionUtil;
import com.toystore.component2_toy.model.Toy;
import com.toystore.component2_toy.service.ToyService;
import com.toystore.component3_order.model.CartItem;
import com.toystore.component3_order.model.Order;
import com.toystore.component3_order.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

// COMPONENT 03: Order Management | Owner: Placeholder
@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final ToyService toyService;
    private final SessionUtil sessionUtil;

    @Autowired
    public OrderController(OrderService orderService, ToyService toyService, SessionUtil sessionUtil) {
        this.orderService = orderService;
        this.toyService = toyService;
        this.sessionUtil = sessionUtil;
    }

    @SuppressWarnings("unchecked")
    private List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam String toyId, @RequestParam int quantity, 
                            HttpSession session, RedirectAttributes redirectAttributes) {
        if (!sessionUtil.isLoggedIn(session)) return "redirect:/users/login";
        
        Toy toy = toyService.getToyById(toyId);
        if (toy == null || toy.getStockQuantity() < quantity) {
            redirectAttributes.addFlashAttribute("errorMessage", "Not enough stock.");
            return "redirect:/toys";
        }

        List<CartItem> cart = getCart(session);
        boolean found = false;
        for (CartItem item : cart) {
            if (item.getToy().getId().equals(toyId)) {
                item.setQuantity(item.getQuantity() + quantity);
                found = true;
                break;
            }
        }
        if (!found) {
            cart.add(new CartItem(toy, quantity));
        }

        redirectAttributes.addFlashAttribute("successMessage", "Added to cart.");
        return "redirect:/toys";
    }

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        if (!sessionUtil.isLoggedIn(session)) return "redirect:/users/login";
        List<CartItem> cart = getCart(session);
        model.addAttribute("cartItems", cart);
        model.addAttribute("subtotal", cart.stream().mapToDouble(CartItem::getSubtotal).sum());
        return "component3_order/cart";
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        if (!sessionUtil.isLoggedIn(session)) return "redirect:/users/login";
        List<CartItem> cart = getCart(session);
        if (cart.isEmpty()) return "redirect:/orders/cart";
        
        String userRole = sessionUtil.getUserRole(session);
        double total = orderService.calculateTotalAmount(cart, userRole);
        model.addAttribute("cartItems", cart);
        model.addAttribute("totalAmount", total);
        return "component3_order/checkout";
    }

    @PostMapping("/checkout")
    public String placeOrder(@RequestParam String deliveryAddress, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!sessionUtil.isLoggedIn(session)) return "redirect:/users/login";
        
        List<CartItem> cart = getCart(session);
        if (cart.isEmpty()) return "redirect:/orders/cart";

        try {
            double total = orderService.calculateTotalAmount(cart, sessionUtil.getUserRole(session));
            orderService.placeOrder(sessionUtil.getLoggedInUserId(session), cart, deliveryAddress, total);
            cart.clear(); // Empty cart
            redirectAttributes.addFlashAttribute("successMessage", "Order placed successfully!");
            return "redirect:/orders/history";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/orders/checkout";
        }
    }

    @GetMapping("/history")
    public String orderHistory(HttpSession session, Model model) {
        if (!sessionUtil.isLoggedIn(session)) return "redirect:/users/login";
        List<Order> orders = orderService.getOrdersByUserId(sessionUtil.getLoggedInUserId(session));
        model.addAttribute("orders", orders);
        return "component3_order/order-list";
    }
    
    @PostMapping("/cancel/{id}")
    public String cancelOrder(@PathVariable String id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!sessionUtil.isLoggedIn(session)) return "redirect:/users/login";
        orderService.cancelOrder(id);
        redirectAttributes.addFlashAttribute("successMessage", "Order cancelled.");
        return "redirect:/orders/history";
    }
}
