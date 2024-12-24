package org.example.webapptest.model;


import jakarta.persistence.*;

@Entity
public class CartItem {

    @EmbeddedId
    private CartItemId id;

    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("product_id")
   // @JoinColumn(name = "product_id", insertable = false, updatable = false, referencedColumnName = "id")
    private Product product;

    // Κατασκευαστές
    public CartItem() {}

    public CartItem(CartItemId id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    // Getters και Setters
    public CartItemId getId() {
        return id;
    }

    public void setId(CartItemId id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}