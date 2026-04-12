package com.smxworld.ecommerce.catalog.internal.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "products", schema = "smx_catalog")
public class Product {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, length = 500)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal price;

    @Column(nullable = false, length = 255)
    private String category;

    @Column(nullable = false)
    private double averageRating = 0.0;

    @Column(nullable = false)
    private int reviewCount = 0;

    @Column(nullable = false)
    private double searchScore = 0.0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Product() {}

    public Product(String name, String description, BigDecimal price, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    @PrePersist
    private void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = Instant.now();
    }

    /**
     * Aggiorna la media del rating con un nuovo voto, usando una media mobile incrementale
     * per evitare di tenere tutti i rating in memoria.
     */
    public void applyNewRating(int rating) {
        reviewCount++;
        averageRating = ((averageRating * (reviewCount - 1)) + rating) / reviewCount;
    }

    public void updateSearchScore(double score) {
        this.searchScore = score;
    }

    public UUID getId()              { return id; }
    public String getName()          { return name; }
    public String getDescription()   { return description; }
    public BigDecimal getPrice()     { return price; }
    public String getCategory()      { return category; }
    public double getAverageRating() { return averageRating; }
    public int getReviewCount()      { return reviewCount; }
    public double getSearchScore()   { return searchScore; }
    public Instant getCreatedAt()    { return createdAt; }
    public Instant getUpdatedAt()    { return updatedAt; }
}
