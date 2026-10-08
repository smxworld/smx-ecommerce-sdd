package com.smxworld.ecommerce.order.internal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

/**
 * JPA entity for a single order line, persisted in {@code smx_order.order_items}.
 *
 * <p>Product name and unit price are snapshotted at checkout so that the order keeps showing what the buyer actually
 * paid even if the catalog changes later. The reference to the product is a plain {@code productId}, not an
 * association, to keep the order module decoupled from the catalog. See product/features/checkout-order.md.
 */
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

    protected OrderItemEntity() {
        // Required by JPA, which instantiates entities reflectively; not meant to be used directly.
    }

    public OrderItemEntity(UUID productId, String productName, BigDecimal unitPrice, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public UUID getId() {
        return id;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity o) {
        this.order = o;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }
}
