package org.example.webapptest;

import java.util.List;
import org.example.webapptest.model.Product;

public class PaymentRequest {

    private String orderId;
    private String customerEmail;
    private String productDetails;  // Αποθήκευση των προϊόντων
    private String postalCode;  // Ταχυδρομικός Κώδικας

    // Getters and Setters
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

   

    // Νέα Getters και Setters για τα πεδία postalCode και weight
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    
}
