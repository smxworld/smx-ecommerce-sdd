package com.smxworld.ecommerce.cart.internal.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

/**
 * JPA aggregate root for a buyer's shopping cart, persisted in {@code smx_cart.carts}.
 *
 * <p>There is one cart per user (unique {@code user_id}) and it owns its {@link CartItemEntity} lines, so that the
 * content and the price snapshots are still there when the buyer returns in a new session. See
 * product/features/cart.md.
 */
@Entity
@Table(name = "carts", schema = "smx_cart")
public class CartEntity {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "user_id", nullable = false, unique = true, length = 255)
    private String userId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CartItemEntity> items = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected CartEntity() {}

    public CartEntity(String userId) {
        this.userId = userId;
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

    public void addItem(CartItemEntity item) {
        item.setCart(this);
        items.add(item);
        this.updatedAt = Instant.now();
    }

    public void removeItem(CartItemEntity item) {
        items.remove(item);
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public List<CartItemEntity> getItems() {
        return items;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
