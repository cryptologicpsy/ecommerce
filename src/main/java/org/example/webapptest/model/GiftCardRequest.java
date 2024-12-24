package org.example.webapptest.model;

public class GiftCardRequest {
    private String orderId;
    private String customerEmail;
    private double giftCardPrice;
    private int giftCardQuantity;

    // Default constructor
    public GiftCardRequest() {
    }

    // Constructor με παραμέτρους
    public GiftCardRequest(String orderId, String customerEmail, double giftCardPrice, int giftCardQuantity) {
        this.orderId = orderId;
        this.customerEmail = customerEmail;
        this.giftCardPrice = giftCardPrice;
        this.giftCardQuantity = giftCardQuantity;
    }

    // Getters and setters
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

    public double getGiftCardPrice() {
        return giftCardPrice;
    }

    public void setGiftCardPrice(double giftCardPrice) {
        this.giftCardPrice = giftCardPrice;
    }

    public int getGiftCardQuantity() {
        return giftCardQuantity;
    }

    public void setGiftCardQuantity(int giftCardQuantity) {
        this.giftCardQuantity = giftCardQuantity;
    }
}
