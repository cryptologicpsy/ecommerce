package org.example.webapptest.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "proionta")

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double discount = 0.0; // Default τιμή
    private double price;
    private int quantity;
    private boolean giftCard;
    private List<String> photoUrls;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "product_sizes")
    @Column(name = "size")
    private List<String> sizes;

    private boolean outOfStock; // Νέο πεδίο για το out of stock

    // Νέα πεδία για διαστάσεις και βάρος
    private double length;
    private double width;
    private double height;
    private double weight;
    @Column(name = "display_order")
    private int displayOrder;


    public Product() {
    }

    public Product(String name, String description, double discount, double price, List<String> photoUrls, int quantity, List<String> sizes, double length, double width, double height, double weight, boolean giftCard) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.photoUrls = photoUrls;  // Αντικατάσταση του πεδίου photoUrl με μια λίστα URLs
        this.quantity = quantity;
        this.sizes = sizes;
        this.outOfStock = false;
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
        this.giftCard = giftCard;
        this.displayOrder = displayOrder;
        this.discount = discount;
    }

    // Getters και Setters για τα νέα πεδία
    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    // Υπάρχοντες Getters και Setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    

    public int getQuantity() {
        return quantity;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public boolean isOutOfStock() {
        return outOfStock;
    }

    public void setOutOfStock(boolean outOfStock) {
        this.outOfStock = outOfStock;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

   

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }

    public boolean isGiftCard() {
        return giftCard;
    }

    public void setGiftCard(boolean giftCard) {
        this.giftCard = giftCard;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
    

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", sizes=" + sizes +
                ", outOfStock=" + outOfStock +
                ", length=" + length +
                ", width=" + width +
                ", height=" + height +
                ", weight=" + weight +
                ", giftCard=" + giftCard +
                ", displayOrder=" + displayOrder +
                '}';
    }
}
