package org.example.webapptest;

import org.example.webapptest.model.Product;
import org.example.webapptest.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Value("${product.image.folder}")
    private String uploadDirectory;


    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.findAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> productOptional = productService.findProductById(id);
        return productOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.ok(savedProduct);
    }

    @PatchMapping("/{id}/updatePriceAndSizes")
    public ResponseEntity<Product> updatePriceAndSizes(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Double newPrice = Double.valueOf(updates.get("price").toString());
        List<String> newSizes = (List<String>) updates.get("sizes");
        Product updatedProduct = productService.updatePriceAndSizes(id, newPrice, newSizes);
        return ResponseEntity.ok(updatedProduct);
    }

    @PatchMapping("/{id}/toggleOutOfStock")
    public ResponseEntity<Product> toggleOutOfStock(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        boolean outOfStock = body.get("outOfStock");
        Product updatedProduct = productService.toggleOutOfStock(id, outOfStock);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        System.out.println("Received request to delete product with ID: " + id);
        try {
            // Εντοπισμός του προϊόντος
            Optional<Product> productOptional = productService.findProductById(id);
            if (productOptional.isEmpty()) {
                System.out.println("Product with ID " + id + " not found.");
                return ResponseEntity.notFound().build();
            }
    
            Product product = productOptional.get();
            System.out.println("Product found: " + product);
    
            // Διαγραφή φωτογραφιών από τον φάκελο
            List<String> photoUrls = product.getPhotoUrls();
            System.out.println("Photo URLs associated with the product: " + photoUrls);
    
            if (photoUrls != null && !photoUrls.isEmpty()) {
                for (String photoUrl : photoUrls) {
                    // Αφαίρεση του "/downloadFile/" από το photoUrl
                    String cleanPhotoUrl = photoUrl.replace("/downloadFile/", "");
                    Path filePath = Paths.get(uploadDirectory, cleanPhotoUrl);
                    File file = new File(filePath.toString());
                    System.out.println("Attempting to delete file: " + file.getAbsolutePath());
    
                    if (file.exists()) {
                        boolean deleted = file.delete();
                        if (deleted) {
                            System.out.println("Successfully deleted file: " + file.getAbsolutePath());
                        } else {
                            System.err.println("Failed to delete file: " + file.getAbsolutePath());
                        }
                    } else {
                        System.err.println("File does not exist: " + file.getAbsolutePath());
                    }
                }
            } else {
                System.out.println("No photo URLs associated with the product.");
            }
    
            // Διαγραφή του προϊόντος από τη βάση δεδομένων
            System.out.println("Deleting product from the database...");
            productService.deleteProduct(id);
            System.out.println("Product deleted successfully.");
    
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Exception occurred while deleting product with ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete product");
        }
    }
    @PutMapping("/{id}/moveUp")
    public ResponseEntity<?> moveProductUp(@PathVariable Long id) {
        return productService.moveProductUp(id);
    }

    @PutMapping("/{id}/moveDown")
    public ResponseEntity<?> moveProductDown(@PathVariable Long id) {
        return productService.moveProductDown(id);
    }

    // Νέο endpoint για το ανέβασμα πολλαπλών εικόνων
    @PostMapping("/uploadImages")
    public ResponseEntity<List<String>> uploadProductImages(@RequestParam("file") MultipartFile[] files) {
        //String uploadDirectory = "/home/cryptologic/Desktop/clowntownfilesupload/uploaded-images/products"; // Αντικατάστησε με την κατάλληλη διαδρομή
        List<String> fileDownloadUris = new ArrayList<>();

        // Ελέγχουμε αν υπάρχουν αρχεία
        if (files == null || files.length == 0) {
            return ResponseEntity.badRequest().body(List.of("No files provided"));
        }

        try {
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();
                if (fileName == null || fileName.isEmpty()) {
                    return ResponseEntity.badRequest().body(List.of("Invalid file name"));
                }

                // Ορίζουμε το path για το ανέβασμα του αρχείου
                Path filePath = Paths.get(uploadDirectory, fileName);
                file.transferTo(filePath.toFile());

                // Δημιουργούμε το URL για το αρχείο
                String fileDownloadUri = "/downloadFile/" + fileName;
                fileDownloadUris.add(fileDownloadUri);
            }

            // Επιστρέφουμε τη λίστα με τα URLs των αρχείων
            return ResponseEntity.ok(fileDownloadUris);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of("Unable to upload files"));
        }
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadProductImage(@RequestParam("file") MultipartFile file) {
        //String uploadDirectory = "/home/cryptologic/Desktop/clowntownfilesupload/uploaded-images/products";
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(uploadDirectory, fileName);

        try {
            file.transferTo(new File(filePath.toString()));
            String fileDownloadUri = "/downloadFile/" + fileName;
            return ResponseEntity.ok(fileDownloadUri);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Unable to upload file");
        }
    }

    @PatchMapping("/{productId}/updateDiscount")
public Product updateDiscount(
        @PathVariable Long productId,
        @RequestBody Map<String, Double> requestBody) {
    Double discount = requestBody.get("discount");
    if (discount == null || discount < 0 || discount > 100) {
        throw new IllegalArgumentException("Το ποσοστό έκπτωσης πρέπει να είναι μεταξύ 0 και 100.");
    }
    return productService.updateDiscount(productId, discount);
}
}
