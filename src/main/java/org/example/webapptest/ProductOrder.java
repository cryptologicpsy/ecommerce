package org.example.webapptest;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


public class ProductOrder {
    private String productId;
    private int quantity;

    public ProductOrder(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getter και Setter για τα πεδία
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ProductOrder{productId='" + productId + "', quantity=" + quantity + '}';
    }
}