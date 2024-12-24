package org.example.webapptest.repository;


import org.example.webapptest.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderId(String orderId);
    // Ανάκτηση παραγγελίας με βάση το paymentIntentId
    Optional<Order> findByPaymentIntentId(String paymentIntentId);
}