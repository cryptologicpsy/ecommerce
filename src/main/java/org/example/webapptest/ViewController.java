package org.example.webapptest;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String homePage() {
        return "productsindex";  // Όνομα του αρχείου HTML χωρίς την επέκταση .html
    }

    @GetMapping("/view-products")
    public String viewProductsPage() {
        return "productsindex";  // Όνομα του αρχείου HTML χωρίς την επέκταση .html
    }

    @GetMapping("/shopping-cart")
    public String showShoppingCart() {
        return "shoppingcart"; // Το όνομα του HTML αρχείου για τη σελίδα του καλαθιού αγορών
    }

    
    @GetMapping("/admin/giftcarddb")
    public String giftcarddb() {
        return "giftcarddb"; // Το όνομα του HTML αρχείου για τη σελίδα του καλαθιού αγορών
    }


    @GetMapping("/productsindex")
    public String productsindex() {
        return "productsindex"; // Το όνομα του HTML αρχείου για τη σελίδα του καλαθιού αγορών
    }
 
    @GetMapping("/about")
    public String about() {
        return "about"; // Το όνομα του HTML αρχείου για τη σελίδα του καλαθιού αγορών
    }
    
    
    @GetMapping("/checkout")
    public String checkout() {
        return "checkout"; // Το όνομα του HTML αρχείου για τη σελίδα του καλαθιού αγορών
    }
    
    
    @GetMapping("/calculate_total")
    public String calculate_total() {
        return "calculate_total"; // Το όνομα του HTML αρχείου για τη σελίδα του καλαθιού αγορών
    }
    
   
    
    
    @GetMapping("/admin")
    public String mainadmin() {
        return "mainadmin"; // Το όνομα του HTML αρχείου για τη σελίδα του καλαθιού αγορών
    }

    @GetMapping("/admin/searchorder")
    public String searchorder() {
        return "searchorder"; // Το όνομα του HTML αρχείου για τη σελίδα του καλαθιού αγορών
    }
 

    @GetMapping("/contact-us")
    public String contactus() {
        return "contact-us"; // Το όνομα του HTML αρχείου για τη σελίδα του καλαθιού αγορών
    }
    
    @GetMapping("/success")
    public String success() {
        return "success"; // Το όνομα του HTML αρχείου για τη σελίδα του καλαθιού αγορών
    }

    @GetMapping("/successorder")
    public String successorder() {
        return "successorder"; // Το όνομα του HTML αρχείου για τη σελίδα του καλαθιού αγορών
    }

   
}
