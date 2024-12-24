package org.example.webapptest.repository;

import java.util.Optional;

import org.example.webapptest.GiftCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GiftCardRepository extends JpaRepository<GiftCard, Long> {

    // Βρίσκουμε την GiftCard με βάση το μοναδικό UUID
    Optional<GiftCard> findByUuid(UUID uuid);
}
