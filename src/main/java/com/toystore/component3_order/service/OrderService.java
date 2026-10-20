package com.toystore.component3_order.service;

import com.toystore.common.IdGenerator;
import com.toystore.component2_toy.service.ToyService;
import com.toystore.component3_order.model.CartItem;
import com.toystore.component3_order.model.Order;
import com.toystore.component3_order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// COMPONENT 03: Order Management | Owner: Placeholder
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ToyService toyService; // Dependency on Component 2 Service allowed

    @Autowired
    public OrderService(OrderRepository orderRepository, ToyService toyService) {
        this.orderRepository = orderRepository;
        this.toyService = toyService;
    }

    // Polymorphism / Abstraction: Calculate final amount based on user type
    public double calculateTotalAmount(List<CartItem> cartItems, String userRole) {
        double subtotal = cartItems.stream().mapToDouble(CartItem::getSubtotal).sum();
        
        // Premium members get 10% discount and free delivery if over Rs 5000
        if ("PREMIUM".equals(userRole)) {
            double discount = subtotal * 0.10;
            double amountAfterDiscount = subtotal - discount;
            double deliveryCharge = (amountAfterDiscount > 5000) ? 0 : 500.0;
            return amountAfterDiscount + deliveryCharge;
        } else {
            // Regular members pay Rs 1000 delivery
            return subtotal + 1000.0;
        }
    }

    public Order placeOrder(String userId, List<CartItem> cartItems, String address, double finalAmount) throws Exception {
        // Validation / Abstraction
        for (CartItem item : cartItems) {
            if (!toyService.checkAndReduceStock(item.getToy().getId(), item.getQuantity())) {
                throw new Exception("Not enough stock for toy: " + item.getToy().getName());
            }
        }

        String itemsSummary = buildItemsSummary(cartItems);
        String id = IdGenerator.generateId("O");
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        
        Order order = new Order(id, userId, itemsSummary, finalAmount, address, "PENDING", date);
        orderRepository.save(order);
        return order;
    }

    private String buildItemsSummary(List<CartItem> cartItems) {
        StringBuilder sb = new StringBuilder();
        for (CartItem item : cartItems) {
            sb.append(item.getToy().getId()).append(":").append(item.getQuantity()).append(",");
        }
        return sb.toString();
    }

    public List<Order> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void updateOrderStatus(String orderId, String newStatus) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(newStatus);
            orderRepository.update(order);
        }
    }

    public void cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null && "PENDING".equals(order.getStatus())) {
            order.setStatus("CANCELLED");
            orderRepository.update(order);
            
            // Restock toys
            String[] items = order.getItemsSummary().split(",");
            for (String itemStr : items) {
                if (!itemStr.isEmpty()) {
                    String[] parts = itemStr.split(":");
                    if (parts.length == 2) {
                        toyService.increaseStock(parts[0], Integer.parseInt(parts[1]));
                    }
                }
            }
        }
    }
}
