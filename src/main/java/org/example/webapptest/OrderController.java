package org.example.webapptest;

import org.example.webapptest.Order;
import org.example.webapptest.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.example.webapptest.Order;
import org.example.webapptest.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    // Αναζήτηση παραγγελίας με βάση το orderId
    @GetMapping("/by-order-id/{orderId}")
    public ResponseEntity<Order> getOrderByOrderId(@PathVariable String orderId) {
        Optional<Order> order = orderRepository.findByOrderId(orderId);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Αναζήτηση παραγγελίας με βάση το paymentIntentId
    @GetMapping("/by-payment-intent-id/{paymentIntentId}")
    public ResponseEntity<Order> getOrderByPaymentIntentId(@PathVariable String paymentIntentId) {
        Optional<Order> order = orderRepository.findByPaymentIntentId(paymentIntentId);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
public List<Order> getAllOrders() {
    return orderRepository.findAll();
}

  // Διαγραφή παραγγελίας με βάση το orderId
  @DeleteMapping("/delete/{orderId}")
  public ResponseEntity<String> deleteOrder(@PathVariable String orderId) {
      // Αναζητούμε την παραγγελία με το orderId
      Optional<Order> orderOptional = orderRepository.findByOrderId(orderId);
      
      // Αν βρεθεί η παραγγελία, την διαγράφουμε
      if (orderOptional.isPresent()) {
          orderRepository.delete(orderOptional.get());
          return ResponseEntity.ok("Order with ID " + orderId + " deleted successfully.");
      } else {
          // Αν η παραγγελία δεν βρεθεί, επιστρέφουμε 404
          return ResponseEntity.status(404).body("Order not found with ID: " + orderId);
      }
  }
}
