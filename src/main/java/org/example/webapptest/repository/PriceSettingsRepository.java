package org.example.webapptest.repository;

import org.example.webapptest.model.PriceSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceSettingsRepository extends JpaRepository<PriceSettingsEntity, Long> {
}