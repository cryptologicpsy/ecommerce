package org.example.webapptest.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class PriceSettingsEntity {
    @Id
    private Long id = 1L; // Ένα μόνο record
    private double basePrice;
    private double sizeMultiplier;
    private double complexityMultiplier;
    private double colorMultiplier;

    // Getters και Setters
    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }
    public double getSizeMultiplier() { return sizeMultiplier; }
    public void setSizeMultiplier(double sizeMultiplier) { this.sizeMultiplier = sizeMultiplier; }
    public double getComplexityMultiplier() { return complexityMultiplier; }
    public void setComplexityMultiplier(double complexityMultiplier) { this.complexityMultiplier = complexityMultiplier; }
    public double getColorMultiplier() { return colorMultiplier; }
    public void setColorMultiplier(double colorMultiplier) { this.colorMultiplier = colorMultiplier; }
}
