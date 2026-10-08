package com.smxworld.ecommerce.review.internal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

/**
 * JPA entity for a product review, persisted in {@code smx_review.reviews}.
 *
 * <p>A buyer can review a product only once (unique constraint on user and product) and only after receiving it, so the
 * entity also records the order that made the review possible. Rating is an integer from 1 to 5. See
 * product/features/reviews-notification.md.
 */
@Entity
@Table(
        name = "reviews",
        schema = "smx_review",
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

    protected ReviewEntity() {
        // Required by JPA, which instantiates entities reflectively; not meant to be used directly.
    }

    public ReviewEntity(UUID productId, String userId, UUID orderId, int rating, String text) {
        this.productId = productId;
        this.userId = userId;
        this.orderId = orderId;
        this.rating = rating;
        this.text = text;
        this.createdAt = Instant.now();
    }

    @PrePersist
    private void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getUserId() {
        return userId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public int getRating() {
        return rating;
    }

    public String getText() {
        return text;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
