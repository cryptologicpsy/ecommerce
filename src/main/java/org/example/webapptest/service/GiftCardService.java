package org.example.webapptest.service;

import org.example.webapptest.GiftCard;
import org.example.webapptest.model.GiftCardRequest;
import org.example.webapptest.repository.GiftCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class GiftCardService {

    private static final Logger logger = LoggerFactory.getLogger(GiftCardService.class);

    @Autowired
    private GiftCardRepository giftCardRepository;

    public void processGiftCards(GiftCardRequest giftCardRequest) {
        logger.info("Processing gift cards for order ID: {}", giftCardRequest.getOrderId());

        String orderId = giftCardRequest.getOrderId();
        String customerEmail = giftCardRequest.getCustomerEmail();
        double giftCardPrice = giftCardRequest.getGiftCardPrice();
        int giftCardQuantity = giftCardRequest.getGiftCardQuantity();

        for (int i = 0; i < giftCardQuantity; i++) {
            GiftCard giftCard = new GiftCard();
            giftCard.setOrderId(orderId);
            giftCard.setCustomerEmail(customerEmail);
            giftCard.setPrice(giftCardPrice);

            logger.info("Saving gift card with UUID: {}", giftCard.getUuid());
            giftCardRepository.save(giftCard);
        }

        logger.info("Successfully processed {} gift cards for order ID: {}", giftCardQuantity, orderId);
    }
}