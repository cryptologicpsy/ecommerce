package org.example.webapptest;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

import org.example.webapptest.service.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final StripeService stripeService;

    @Autowired
    public CheckoutController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/create-session")
    public String createCheckoutSession(@RequestBody CheckoutRequest checkoutRequest) {
        try {
            Session session = stripeService.createCheckoutSession(checkoutRequest.getAmount(), checkoutRequest.getCurrency());
            return session.getUrl();
        } catch (StripeException e) {
            e.printStackTrace();
            return "Error occurred while creating checkout session";
        }
    }

    public static class CheckoutRequest {
        private Double amount;
        private String currency;

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }
}


