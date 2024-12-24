package org.example.webapptest;


import org.example.webapptest.model.GiftCardRequest;
import org.example.webapptest.service.GiftCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/giftcards")
public class GiftCardController {

    @Autowired
    private GiftCardService giftCardService;

    // Μέθοδος για την επεξεργασία και αποθήκευση των gift cards
    @PostMapping
    public ResponseEntity<String> createGiftCard(@RequestBody GiftCardRequest giftCardRequest) {
        try {
            // Επεξεργασία και αποθήκευση των gift cards
            giftCardService.processGiftCards(giftCardRequest);
            return ResponseEntity.ok("Gift card created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating gift card.");
        }
    }
}