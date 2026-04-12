package com.smxworld.ecommerce.review.internal.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "reviews", schema = "smx_review",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"}))
public class ReviewEntity {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "user_id", nullable = false, length = 255)
    private String userId;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(nullable = false)
    private int rating;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected ReviewEntity() {}

    public ReviewEntity(UUID productId, String userId, UUID orderId, int rating, String text) {
        this.productId = productId;
        this.userId    = userId;
        this.orderId   = orderId;
        this.rating    = rating;
        this.text      = text;
        this.createdAt = Instant.now();
    }

    @PrePersist
    private void onCreate() {
        if (this.createdAt == null) this.createdAt = Instant.now();
    }

    public UUID getId()        { return id; }
    public UUID getProductId() { return productId; }
    public String getUserId()  { return userId; }
    public UUID getOrderId()   { return orderId; }
    public int getRating()     { return rating; }
    public String getText()    { return text; }
    public Instant getCreatedAt() { return createdAt; }
}
