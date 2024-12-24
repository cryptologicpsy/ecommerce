package org.example.webapptest.service;

import org.example.webapptest.exception.ResourceNotFoundException;
import org.example.webapptest.model.Product;
import org.example.webapptest.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public String fetchProductName(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        return productOptional.map(Product::getName).orElse("Unknown Product");
    }


    public List<Product> findAllProducts() {
        return productRepository.findAllByOrderByDisplayOrderAsc();
    }

    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public Product saveProduct(Product product) {
        // Έλεγχος αν το προϊόν είναι καινούριο (χωρίς ID)
        if (product.getId() == null) {
            // Ανάκτηση της μέγιστης τιμής του display order από το repository
            Integer maxDisplayOrder = productRepository.findMaxDisplayOrder();
            
            // Ορισμός του display order: Αν το maxDisplayOrder είναι null, ξεκινάμε από το 1
            product.setDisplayOrder((maxDisplayOrder != null) ? maxDisplayOrder + 1 : 1);
        }
    
        // Αποθήκευση του προϊόντος στο repository
        return productRepository.save(product);
    }

    @Transactional
    public Product updatePriceAndSizes(Long id, Double newPrice, List<String> newSizes) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + id));

        product.setPrice(newPrice);
        product.setSizes(newSizes);
        return productRepository.save(product);
    }

    @Transactional
    public Product toggleOutOfStock(Long id, boolean outOfStock) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + id));
        product.setOutOfStock(outOfStock);
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + id));
        productRepository.delete(product);
    }

    @Transactional
    public ResponseEntity<?> moveProductUp(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    int currentDisplayOrder = product.getDisplayOrder();
                    if (currentDisplayOrder > 1) {
                        Product productToSwapWith = productRepository.findByDisplayOrder(currentDisplayOrder - 1);
                        if (productToSwapWith != null) {
                            product.setDisplayOrder(currentDisplayOrder - 1);
                            productToSwapWith.setDisplayOrder(currentDisplayOrder);

                            productRepository.save(product);
                            productRepository.save(productToSwapWith);

                            return ResponseEntity.ok().build();
                        }
                    }
                    return ResponseEntity.badRequest().body("Cannot move product further up");
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<?> moveProductDown(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    int currentDisplayOrder = product.getDisplayOrder();
                    Integer maxDisplayOrder = productRepository.findMaxDisplayOrder();
                    if (currentDisplayOrder < maxDisplayOrder) {
                        Product productToSwapWith = productRepository.findByDisplayOrder(currentDisplayOrder + 1);
                        if (productToSwapWith != null) {
                            product.setDisplayOrder(currentDisplayOrder + 1);
                            productToSwapWith.setDisplayOrder(currentDisplayOrder);

                            productRepository.save(product);
                            productRepository.save(productToSwapWith);

                            return ResponseEntity.ok().build();
                        }
                    }
                    return ResponseEntity.badRequest().body("Cannot move product further down");
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public Product updateDiscount(Long productId, Double discount) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        product.setDiscount(discount); // Υποθέτοντας ότι το Product έχει πεδίο discount
        return productRepository.save(product);
    }
}
