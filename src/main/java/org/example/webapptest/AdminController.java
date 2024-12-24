package org.example.webapptest;
import org.example.webapptest.model.Product;
import org.example.webapptest.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductService productService;

    @PostMapping("/products")
    public Product addProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }
}