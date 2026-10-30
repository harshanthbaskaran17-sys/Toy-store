package com.toystore.component3_order.repository;

import com.toystore.common.FileHandler;
import com.toystore.component3_order.model.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// COMPONENT 03: Order Management | Owner: Placeholder
@Repository
public class OrderRepository {
    private final FileHandler fileHandler;

    public OrderRepository() {
        this.fileHandler = new FileHandler("orders.txt");
    }

    public synchronized void save(Order order) {
        fileHandler.appendLine(order.toFileString());
    }

    public synchronized List<Order> findAll() {
        List<String> lines = fileHandler.readAllLines();
        List<Order> orders = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 7) {
                orders.add(new Order(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]), parts[4], parts[5], parts[6]));
            }
        }
        return orders;
    }

    public Optional<Order> findById(String id) {
        return findAll().stream()
                .filter(o -> o.getId().equals(id))
                .findFirst();
    }

    public List<Order> findByUserId(String userId) {
        return findAll().stream()
                .filter(o -> o.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public synchronized void update(Order updatedOrder) {
        List<Order> orders = findAll();
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getId().equals(updatedOrder.getId())) {
                orders.set(i, updatedOrder);
                break;
            }
        }
        saveAll(orders);
    }
    
    public synchronized void delete(String id) {
        List<Order> orders = findAll().stream()
                .filter(o -> !o.getId().equals(id))
                .collect(Collectors.toList());
        saveAll(orders);
    }

    private void saveAll(List<Order> orders) {
        List<String> lines = orders.stream().map(Order::toFileString).collect(Collectors.toList());
        fileHandler.writeAllLines(lines);
    }
}
