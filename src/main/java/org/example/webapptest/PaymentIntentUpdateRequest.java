package org.example.webapptest;

import java.util.Map;

public class PaymentIntentUpdateRequest {

    private String paymentIntentId;
    //private Map<String, String> metadata;  // Αλλάξτε το σε Map<String, String> για να είναι συμβατό με την Stripe

    // Getters
    public String getPaymentIntentId() {
        return paymentIntentId;
    }

    //public Map<String, String> getMetadata() {
    //    return metadata;
   // }

    // Setters
    public void setPaymentIntentId(String paymentIntentId) {
        this.paymentIntentId = paymentIntentId;
    }

   // public void setMetadata(Map<String, String> metadata) {
   //     this.metadata = metadata;
   // }
}
