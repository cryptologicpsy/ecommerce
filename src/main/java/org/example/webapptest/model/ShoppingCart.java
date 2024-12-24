package org.example.webapptest.model;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {

    private final Map<Long, CartItem> items = new HashMap<>();

    public ShoppingCart() {
    }

    // Προσθέτει ένα προϊόν στο καλάθι ή αυξάνει την ποσότητα
    public void addProduct(Product product, int quantity) {
        CartItem item = items.get(product.getId());

        if (item == null) {
            item = new CartItem(new CartItemId(product.getId(), "some-session-id"), quantity);
            items.put(product.getId(), item);
        } else {
            item.setQuantity(item.getQuantity() + quantity);
        }
    }

    // Αφαιρεί ένα προϊόν από το καλάθι
    public void removeProduct(Product product) {
        items.remove(product.getId());
    }

    // Ενημερώνει την ποσότητα ενός προϊόντος στο καλάθι
    public void updateProductQuantity(Product product, int quantity) {
        CartItem item = items.get(product.getId());
        if (item != null) {
            item.setQuantity(quantity);
        }
    }

    // Επιστρέφει το καλάθι αγορών
    public Map<Long, CartItem> getItems() {
        return items;
    }

    // Καθαρίζει το καλάθι
    public void clear() {
        items.clear();
    }

    // Υπολογίζει το συνολικό κόστος των προϊόντων στο καλάθι
    public double getTotalCost() {
        double totalCost = 0.0;
        for (CartItem item : items.values()) {
            Product product = item.getProduct(); // Υποθέτουμε ότι υπάρχει μια μέθοδος getProduct() στην CartItem
            totalCost += item.getQuantity() * product.getPrice();
        }
        return totalCost;
    }
}