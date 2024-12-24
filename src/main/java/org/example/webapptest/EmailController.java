package org.example.webapptest;

import java.util.List;
import java.util.Map;

import org.example.webapptest.service.OrderEmailSender;
import org.example.webapptest.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EmailController {

    @Value("${admin.email}")
    private String adminEmail;

    @Autowired
    private OrderEmailSender emailSender;

    @Autowired
    private ProductService productService;

    @PostMapping("/send-success-email")
    public void sendSuccessEmail(@RequestBody Map<String, Object> request) {
        System.out.println("Received request: " + request);

        Map<String, String> customerDetails = (Map<String, String>) request.get("customerDetails");
        List<Map<String, Object>> basket = (List<Map<String, Object>>) request.get("basket");

        StringBuilder emailContent = new StringBuilder();
        emailContent.append("Customer Details:\n");
        emailContent.append("First Name: ").append(customerDetails.get("firstName")).append("\n");
        emailContent.append("Last Name: ").append(customerDetails.get("lastName")).append("\n");
        emailContent.append("Company: ").append(customerDetails.get("company")).append("\n");
        emailContent.append("Country: ").append(customerDetails.get("country")).append("\n");
        emailContent.append("Address1: ").append(customerDetails.get("address1")).append("\n");
        emailContent.append("Address2: ").append(customerDetails.get("address2")).append("\n");
        emailContent.append("City: ").append(customerDetails.get("city")).append("\n");
        emailContent.append("State: ").append(customerDetails.get("state")).append("\n");
        emailContent.append("Zip: ").append(customerDetails.get("zip")).append("\n");
        emailContent.append("Phone: ").append(customerDetails.get("phone")).append("\n");
        emailContent.append("Email: ").append(customerDetails.get("email")).append("\n");
        emailContent.append("Order Notes: ").append(customerDetails.get("orderNotes")).append("\n\n");

        emailContent.append("Product Details:\n");
        for (Map<String, Object> item : basket) {
        	Long productId = Long.parseLong((String) item.get("product_id"));

            // Καλέστε τη μέθοδο fetchProductName μέσω του αντικειμένου productService
            String productName = productService.fetchProductName(productId);

            emailContent.append("Product ID: ").append(productId).append("\n");
            emailContent.append("Product Name: ").append(productName).append("\n");
            emailContent.append("Size: ").append(item.get("size")).append("\n");
            emailContent.append("Quantity: ").append(item.get("quantity")).append("\n\n");
        }

        // Στείλτε email στον admin
        emailSender.sendOrderEmail(adminEmail, "New Order Success", emailContent.toString());

        // Δημιουργήστε περιεχόμενο email για τον πελάτη
        StringBuilder customerEmailContent = new StringBuilder();
        customerEmailContent.append("Αγαπητέ/ή ").append(customerDetails.get("firstName")).append(" ").append(customerDetails.get("lastName")).append(",\n\n");
        customerEmailContent.append("Η παραγγελία σας ολοκληρώθηκε επιτυχώς!\n");
        customerEmailContent.append("Τα στοιχεία της παραγγελίας σας είναι:\n\n");
        customerEmailContent.append(emailContent); // Προσθέστε τα ίδια στοιχεία παραγγελίας

        // Στείλτε email στον πελάτη
        emailSender.sendCustomerEmail(customerDetails.get("email"), "Η παραγγελία σας ολοκληρώθηκε επιτυχώς", customerEmailContent.toString());
    }
}
