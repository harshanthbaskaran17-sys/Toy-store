package com.toystore.component3_order.model;

import java.time.LocalDateTime;

// COMPONENT 03: Order Management | Owner: Placeholder
public class Order {
    private String id;
    private String userId;
    private String itemsSummary; // Format: toyId1:qty1,toyId2:qty2
    private double totalAmount;
    private String deliveryAddress;
    private String status; // PENDING, SHIPPED, DELIVERED, CANCELLED
    private String orderDate;

    public Order(String id, String userId, String itemsSummary, double totalAmount, String deliveryAddress, String status, String orderDate) {
        this.id = id;
        this.userId = userId;
        this.itemsSummary = itemsSummary;
        this.totalAmount = totalAmount;
        this.deliveryAddress = deliveryAddress;
        this.status = status;
        this.orderDate = orderDate;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getItemsSummary() { return itemsSummary; }
    public void setItemsSummary(String itemsSummary) { this.itemsSummary = itemsSummary; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }

    public String toFileString() {
        return id + "|" + userId + "|" + itemsSummary + "|" + totalAmount + "|" + deliveryAddress + "|" + status + "|" + orderDate;
    }
}
