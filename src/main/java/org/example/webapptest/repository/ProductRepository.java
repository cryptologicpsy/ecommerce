package org.example.webapptest.repository;


import java.util.List;

import org.example.webapptest.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Εδώ μπορείτε να προσθέσετε προσαρμοσμένες μεθόδους ερωτημάτων, αν χρειάζεται

    @Query("SELECT COALESCE(MAX(a.displayOrder), 0) FROM Product a")
    Integer findMaxDisplayOrder();

        Product findByDisplayOrder(int displayOrder); // Χρησιμοποιήστε αυτήν τη μέθοδο για τις ανταλλαγές

    List<Product> findAllByOrderByDisplayOrderAsc();
}