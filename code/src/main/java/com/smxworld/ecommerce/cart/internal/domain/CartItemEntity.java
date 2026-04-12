package com.smxworld.ecommerce.cart.internal.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

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

    protected CartItemEntity() {}

    public CartItemEntity(UUID productId, String productName, int quantity, BigDecimal unitPrice) {
        this.productId   = productId;
        this.productName = productName;
        this.quantity    = quantity;
        this.unitPrice   = unitPrice;
        this.addedAt     = Instant.now();
    }

    public UUID getId()               { return id; }
    public CartEntity getCart()       { return cart; }
    public void setCart(CartEntity c) { this.cart = c; }
    public UUID getProductId()        { return productId; }
    public String getProductName()    { return productName; }
    public int getQuantity()          { return quantity; }
    public void setQuantity(int q)    { this.quantity = q; }
    public BigDecimal getUnitPrice()  { return unitPrice; }
    public Instant getAddedAt()       { return addedAt; }
}
