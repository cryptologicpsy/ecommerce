package org.example.webapptest;


import org.example.webapptest.service.ShippingCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipping")
public class ShippingCalculatorController {

    @Autowired
    private ShippingCalculatorService shippingCalculatorService;

    @PostMapping("/calculate")
    public double calculateShipping(@RequestBody ShippingRequest shippingRequest) {
        return shippingCalculatorService.calculateShippingCost(shippingRequest.getPostalCode(), shippingRequest.getWeight());
    }
}

class ShippingRequest {
    private String postalCode;
    private double weight;

    // getters and setters
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
