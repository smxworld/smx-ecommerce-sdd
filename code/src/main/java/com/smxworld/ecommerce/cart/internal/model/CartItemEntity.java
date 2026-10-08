package com.smxworld.ecommerce.cart.internal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

/**
 * JPA entity for a line of a {@link CartEntity}, persisted in {@code smx_cart.cart_items}.
 *
 * <p>It stores the product name and the unit price at the time the product was added, so the cart shows stable prices;
 * they are not recomputed from the catalog. See product/features/cart.md.
 */
@Entity
@Table(name = "cart_items", schema = "smx_cart")
public class CartItemEntity {

    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private CartEntity cart;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "product_name", nullable = false, length = 500)
    private String productName;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "unit_price", nullable = false, precision = 19, scale = 4)
    private BigDecimal unitPrice;

    @Column(name = "added_at", nullable = false, updatable = false)
    private Instant addedAt;

    protected CartItemEntity() {
        // Required by JPA, which instantiates entities reflectively; not meant to be used directly.
    }

    public CartItemEntity(UUID productId, String productName, int quantity, BigDecimal unitPrice) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.addedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    void setCart(CartEntity c) {
        this.cart = c;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int q) {
        this.quantity = q;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public Instant getAddedAt() {
        return addedAt;
    }
}
