package org.example.webapptest;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "cards")
public class GiftCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId; // Αναφορά στην παραγγελία

    private String customerEmail;

    private double giftCardPrice;

    private Long productId;  // ID του προϊόντος για την κάρτα δώρου
    


    @Column(unique = true, nullable = false)
    private UUID uuid;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public double getPrice() {
        return giftCardPrice;
    }

    public void setPrice(double giftCardPrice) {
        this.giftCardPrice = giftCardPrice;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
