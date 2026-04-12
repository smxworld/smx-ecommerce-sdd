package com.smxworld.ecommerce.order.internal.domain;

import com.smxworld.ecommerce.order.OrderStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders", schema = "smx_order")
public class OrderEntity {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "user_id", nullable = false, length = 255)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private OrderStatus status;

    @Column(name = "total_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal totalAmount;

    @Column(name = "shipping_address", columnDefinition = "TEXT")
    private String shippingAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderItemEntity> items = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected OrderEntity() {}

    public OrderEntity(String userId, String shippingAddress) {
        this.userId          = userId;
        this.shippingAddress = shippingAddress;
        this.status          = OrderStatus.PENDING;
        this.totalAmount     = BigDecimal.ZERO;
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

    public void addItem(OrderItemEntity item) {
        item.setOrder(this);
        items.add(item);
        recalculateTotal();
    }

    public void transitionTo(OrderStatus newStatus) {
        this.status    = newStatus;
        this.updatedAt = Instant.now();
    }

    private void recalculateTotal() {
        this.totalAmount = items.stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public UUID getId()                       { return id; }
    public String getUserId()                 { return userId; }
    public OrderStatus getStatus()            { return status; }
    public BigDecimal getTotalAmount()         { return totalAmount; }
    public String getShippingAddress()         { return shippingAddress; }
    public List<OrderItemEntity> getItems()   { return items; }
    public Instant getCreatedAt()             { return createdAt; }
    public Instant getUpdatedAt()             { return updatedAt; }
}
