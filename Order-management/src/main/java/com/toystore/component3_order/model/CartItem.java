package com.toystore.component3_order.model;

import com.toystore.component2_toy.model.Toy;

// COMPONENT 03: Order Management | Owner: Placeholder
public class CartItem {
    private Toy toy;
    private int quantity;

    public CartItem(Toy toy, int quantity) {
        this.toy = toy;
        this.quantity = quantity;
    }

    public Toy getToy() { return toy; }
    public void setToy(Toy toy) { this.toy = toy; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getSubtotal() {
        return toy.getPrice() * quantity;
    }
}
