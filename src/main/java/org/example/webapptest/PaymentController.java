package org.example.webapptest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import org.example.webapptest.repository.GiftCardRepository;
import org.example.webapptest.repository.OrderRepository;
import org.example.webapptest.repository.ProductRepository;
import org.example.webapptest.service.ShippingCalculatorService;
import org.example.webapptest.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

     @Autowired
    private JavaMailSender javaMailSender; // Εξάρτηση για αποστολή email


    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private GiftCardRepository giftCardRepository;

    @Autowired
    private ShippingCalculatorService shippingCalculatorService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Υπολογισμός συνολικού βάρους προϊόντων από το JSON.
     */
    private double calculateTotalWeight(String productDetailsJson) {
        double totalWeight = 0.0;
        try {
            List<Map<String, Object>> productDetails = objectMapper.readValue(productDetailsJson, List.class);
            for (Map<String, Object> product : productDetails) {
                Long productId = Long.parseLong(product.get("product_id").toString());
                int quantity = Integer.parseInt(product.get("quantity").toString());

                Product productEntity = productRepository.findById(productId)
                        .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

                totalWeight += productEntity.getWeight() * quantity;
            }
        } catch (JsonProcessingException e) {
            logger.error("Error parsing product details JSON: {}", e.getMessage());
        } catch (NumberFormatException e) {
            logger.error("Invalid product_id or quantity format: {}", e.getMessage());
        }
        return totalWeight;
    }

    /**
 * Υπολογισμός συνολικού ποσού, συμπεριλαμβανομένου του κόστους αποστολής.
 */
private long calculateTotalAmount(String productDetailsJson, double shippingCost) {
    long totalAmount = 0;
    boolean containsOnlyGiftCards = true; // Υποθέτουμε ότι είναι μόνο gift cards

    try {
        List<Map<String, Object>> productDetails = objectMapper.readValue(productDetailsJson, List.class);
        for (Map<String, Object> product : productDetails) {
            Long productId = Long.parseLong(product.get("product_id").toString());
            int quantity = Integer.parseInt(product.get("quantity").toString());

            Product productEntity = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

            // Ελέγχουμε αν το προϊόν είναι κανονικό ή gift card
            if (!productEntity.isGiftCard()) {
                containsOnlyGiftCards = false; // Αν βρούμε κανονικό προϊόν, το καλάθι δεν περιέχει μόνο gift cards
            }

            long productCost = Math.round(productEntity.getPrice() * 100) * quantity;
            totalAmount += productCost;
        }

        // Αν το καλάθι περιέχει μόνο gift cards, το shipping cost είναι 0
        if (containsOnlyGiftCards) {
            shippingCost = 0;
        }

    } catch (JsonProcessingException e) {
        logger.error("Error parsing product details JSON: {}", e.getMessage());
    } catch (NumberFormatException e) {
        logger.error("Invalid product_id or quantity format: {}", e.getMessage());
    }

    long shippingCostInCents = (long) (shippingCost * 100);
    totalAmount += shippingCostInCents;

    return totalAmount;
}

    /**
     * Δημιουργία PaymentIntent.
     */
    @PostMapping("/payment-intent")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody PaymentRequest paymentRequest) {
        String orderId = paymentRequest.getOrderId();
        String customerEmail = paymentRequest.getCustomerEmail();
        String productDetails = paymentRequest.getProductDetails(); // Παίρνουμε το JSON των προϊόντων
        String postalCode = paymentRequest.getPostalCode();

        if (orderId == null || orderId.isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "orderId cannot be null or empty.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        // Υπολογισμός συνολικού βάρους και ποσού
        double weight = calculateTotalWeight(productDetails);
        double shippingCost = shippingCalculatorService.calculateShippingCost(postalCode, weight);
        long totalAmount = calculateTotalAmount(productDetails, shippingCost);

        logger.info("Creating PaymentIntent for order {} with total amount {}", orderId, totalAmount);

        try {
            Stripe.apiKey = stripeSecretKey;

            Map<String, String> metadata = new HashMap<>();
            metadata.put("orderId", orderId);
            metadata.put("customerEmail", customerEmail);

            PaymentIntent paymentIntent = PaymentIntent.create(PaymentIntentCreateParams.builder()
                    .setAmount(totalAmount)
                    .setCurrency("eur")
                    .putAllMetadata(metadata)
                    .build());

            // Δημιουργία παραγγελίας και αποθήκευση στη βάση δεδομένων
            createOrder(paymentIntent, orderId, customerEmail, productDetails);

            Map<String, String> responseData = new HashMap<>();
            responseData.put("clientSecret", paymentIntent.getClientSecret());
            responseData.put("totalAmount", String.valueOf(totalAmount));
            responseData.put("shippingCost", String.valueOf(shippingCost));

            logger.info("PaymentIntent created successfully with clientSecret: {}", paymentIntent.getClientSecret());
            return ResponseEntity.ok(responseData);
        } catch (StripeException e) {
            logger.error("Failed to create PaymentIntent: {}", e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to create payment intent: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Επιβεβαίωση πληρωμής και αποθήκευση  gift cards.
     */
    @PostMapping("/confirm-payment")
    public ResponseEntity<String> confirmPayment(@RequestBody PaymentConfirmationRequest paymentConfirmationRequest) {
        try {
            String paymentIntentId = paymentConfirmationRequest.getPaymentIntentId();
            String orderId = paymentConfirmationRequest.getOrderId();
        
            // Ανάκτηση της παραγγελίας από τη βάση δεδομένων χρησιμοποιώντας το orderId
            Order order = orderRepository.findByPaymentIntentId(paymentIntentId)
                    .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            logger.info("Retrieved PaymentIntent with ID: {}", paymentIntentId);
        
            if ("succeeded".equals(paymentIntent.getStatus())) {
                // Μόνο καταγραφή της επιτυχούς πληρωμής
                order.setPaymentStatus("paid"); // Προσθήκη κατάστασης πληρωμής
                orderRepository.save(order);
                
                // Αν η παραγγελία περιέχει gift cards, προσθέτουμε τις αντίστοιχες εγγραφές
                processGiftCards(order);
    
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment not successful");
            }
        
            return ResponseEntity.ok("Payment confirmed");
        } catch (Exception e) {
            logger.error("Error confirming payment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error confirming payment");
        }
    }

/**
 * Διαχείριση των gift cards και καταχώρηση τους στη βάση δεδομένων.
 */
private void processGiftCards(Order order) {
    try {
        // Παίρνουμε τα προϊόντα της παραγγελίας
        List<Map<String, Object>> productDetails = objectMapper.readValue(order.getProductDetails(), List.class);
        
        for (Map<String, Object> product : productDetails) {
            Long productId = Long.parseLong(product.get("product_id").toString());
            int quantity = Integer.parseInt(product.get("quantity").toString());
    
            Product productEntity = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
    
            // Αν το προϊόν είναι gift card, το καταχωρούμε στην αντίστοιχη οντότητα
            if (productEntity.isGiftCard()) {
                double giftCardPrice = productEntity.getPrice();
    
                // Εισάγουμε το gift card στην βάση με την ποσότητα που υπάρχει
                for (int i = 0; i < quantity; i++) {
                    GiftCard giftCard = new GiftCard();
                    giftCard.setOrderId(order.getOrderId());
                    giftCard.setCustomerEmail(order.getCustomerEmail());
                    giftCard.setProductId(productId);
                    giftCard.setPrice(giftCardPrice);
                    giftCard.setUuid(UUID.randomUUID());
                    giftCardRepository.save(giftCard); // Αποθήκευση στη βάση

                    sendGiftCardEmail(order.getCustomerEmail(), giftCard);
                }
                logger.info("GiftCard(s) added to database for Order ID: {}", order.getOrderId());
            }
        }
    } catch (Exception e) {
        logger.error("Error processing gift cards: {}", e.getMessage());
    }
}

    /**
     * Δημιουργία παραγγελίας
     */
    private void createOrder(PaymentIntent paymentIntent, String orderId, String customerEmail, String productDetails) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setCustomerEmail(customerEmail);
        order.setPaymentIntentId(paymentIntent.getId());
        order.setProductDetails(productDetails); // Αποθήκευση του productDetails
        logger.info("Order created with ID: {}", orderId);
        orderRepository.save(order);
    }

       private void sendGiftCardEmail(String customerEmail, GiftCard giftCard) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(customerEmail);
            helper.setSubject("Your Gift Card is Ready!");
            helper.setText(buildEmailContent(giftCard), true);

            javaMailSender.send(message);
            logger.info("Email sent to {} for Gift Card UUID: {}", customerEmail, giftCard.getUuid());
        } catch (MessagingException e) {
            logger.error("Failed to send email to {}: {}", customerEmail, e.getMessage());
        }
    }

    /**
     * Δημιουργία περιεχομένου email για τη gift card.
     */
    private String buildEmailContent(GiftCard giftCard) {
        return String.format(
                "<html>" +
                "<body>" +
                "<h1>Thank you for your purchase!</h1>" +
                "<p>Your gift card is ready:</p>" +
                "<p><strong>Gift Card Code:</strong> %s</p>" +
                "<p><strong>Price:</strong> €%.2f</p>" +
                "<p>We hope you enjoy your experience with us.</p>" +
                "</body>" +
                "</html>",
                giftCard.getUuid(),
                giftCard.getPrice()
        );
    }

    @GetMapping("/admin/giftcards")
public List<GiftCard> getAllGiftCards() {
    return giftCardRepository.findAll();
}
@GetMapping("/admin/giftcards/search")
public List<GiftCard> searchGiftCards(@RequestParam String uuid) {
    return giftCardRepository.findAll().stream()
        .filter(card -> card.getUuid().toString().startsWith(uuid))
        .collect(Collectors.toList());
}

@DeleteMapping("/admin/giftcards/{uuid}")
public ResponseEntity<Void> deleteGiftCard(@PathVariable UUID uuid) {
    giftCardRepository.findByUuid(uuid).ifPresent(giftCardRepository::delete);
    return ResponseEntity.noContent().build();
}



}

