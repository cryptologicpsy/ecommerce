package org.example.webapptest.webconfig;

import org.example.webapptest.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Ενεργοποίηση ή απενεργοποίηση CSRF με Lambda
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration corsConfig = new CorsConfiguration();
                corsConfig.addAllowedOrigin("*");
                corsConfig.addAllowedMethod("*");
                corsConfig.addAllowedHeader("*");
                return corsConfig;
            })) // Ρύθμιση CORS
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/sign-up/**", "/sign-in/**").permitAll() // Ανοιχτά
                .requestMatchers("/admin/**").authenticated() // Απαιτείται authentication για admin
                .requestMatchers("/users").authenticated() // Απαιτείται authentication για το /users
                .requestMatchers(
                    "/products/add", 
            
                    "/products/{id}/updatePriceAndSizes", 
                    "/products/{id}/toggleOutOfStock", 
                    "/products/delete/{id}",
                    "/products/{id}/moveUp", 
                    "/products/{id}/moveDown", 
                    "/products/uploadImages", 
                    "/products/uploadImage", 
                    "/products/{productId}/updateDiscount",
                   
                   
                    "/api/update-prices",
                    "api/orders/by-payment-intent-id/{paymentIntentId}",
                    "/api/orders/by-order-id/{orderId}",
                    "/api/orders/all",
                    "/api/giftcards",
                    "/api/admin/giftcards",
                    "/api/admin/giftcards/search",
                    "/api/admin/giftcards/{uuid}",
                    "/api/orders/delete/{orderId}"
                    )
                .authenticated() // Προστασία για τα παρακάτω endpoints
                .requestMatchers(
                    "/products", 
                    "/products/{id}"
                   
                ).permitAll() // Ανοιχτά για το κοινό
                .anyRequest().permitAll() // Ειδική προστασία για όλα τα υπόλοιπα αιτήματα
            )
            .formLogin(form -> form
                .loginPage("/sign-in")
                .permitAll()) // Ορίζουμε τη σελίδα login
        ;
    
        return http.build();
    }


   

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
            .passwordEncoder(bCryptPasswordEncoder);
    }
}
