package org.example.webapptest;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminViewController {

    @GetMapping("/admin/admin")
    public String adminPage() {
        return "admin";  // Όνομα του αρχείου HTML χωρίς την επέκταση .html
    }
    
    
    @GetMapping("/admin/adminb")
    public String adminPageb() {
        return "adminb";  // Όνομα του αρχείου HTML χωρίς την επέκταση .html
    }
}
