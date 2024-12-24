package org.example.webapptest.model;

import java.util.List;

public class PaymentConfirmationRequest {

    private String paymentIntentId;
    private String orderId;
    private String customerEmail;
    private String productDetails; // JSON string that contains the details of the products

    // Constructor
    public PaymentConfirmationRequest() {}

    // Getters and Setters

    public String getPaymentIntentId() {
        return paymentIntentId;
    }

    public void setPaymentIntentId(String paymentIntentId) {
        this.paymentIntentId = paymentIntentId;
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

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    @Override
    public String toString() {
        return "PaymentConfirmationRequest{" +
                "paymentIntentId='" + paymentIntentId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", productDetails='" + productDetails + '\'' +
                '}';
    }
}
