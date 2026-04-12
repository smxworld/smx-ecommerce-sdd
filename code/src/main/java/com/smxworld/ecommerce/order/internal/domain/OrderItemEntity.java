package com.smxworld.ecommerce.order.internal.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_items", schema = "smx_order")
public class OrderItemEntity {

    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "product_name", nullable = false, length = 500)
    private String productName;

    @Column(name = "unit_price", nullable = false, precision = 19, scale = 4)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private int quantity;

    protected OrderItemEntity() {}

    public OrderItemEntity(UUID productId, String productName, BigDecimal unitPrice, int quantity) {
        this.productId   = productId;
        this.productName = productName;
        this.unitPrice   = unitPrice;
        this.quantity    = quantity;
    }

    public UUID getId()              { return id; }
    public OrderEntity getOrder()    { return order; }
    public void setOrder(OrderEntity o) { this.order = o; }
    public UUID getProductId()       { return productId; }
    public String getProductName()   { return productName; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public int getQuantity()         { return quantity; }
}
