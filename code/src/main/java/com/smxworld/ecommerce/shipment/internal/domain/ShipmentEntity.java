package com.smxworld.ecommerce.shipment.internal.domain;

import com.smxworld.ecommerce.shipment.ShipmentStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "shipments", schema = "smx_shipment")
public class ShipmentEntity {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "order_id", nullable = false, unique = true)
    private UUID orderId;

    @Column(name = "user_id", nullable = false, length = 255)
    private String userId;

    @Column(name = "tracking_number", nullable = false, length = 100)
    private String trackingNumber;

    @Column(nullable = false, length = 100)
    private String carrier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ShipmentStatus status;

    @Column(name = "shipped_at")
    private Instant shippedAt;

    @Column(name = "estimated_delivery")
    private Instant estimatedDelivery;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected ShipmentEntity() {}

    public ShipmentEntity(UUID orderId, String userId) {
        this.orderId        = orderId;
        this.userId         = userId;
        this.trackingNumber = "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.carrier        = "SimulatedCarrier";
        this.status         = ShipmentStatus.IN_TRANSIT;
        this.shippedAt      = Instant.now();
        this.estimatedDelivery = Instant.now().plusSeconds(7L * 24 * 3600);
        this.createdAt      = Instant.now();
    }

    @PrePersist
    private void onCreate() {
        if (this.createdAt == null) this.createdAt = Instant.now();
    }

    public void markDelivered() {
        this.status = ShipmentStatus.DELIVERED;
    }

    public UUID getId()                   { return id; }
    public UUID getOrderId()              { return orderId; }
    public String getUserId()             { return userId; }
    public String getTrackingNumber()     { return trackingNumber; }
    public String getCarrier()            { return carrier; }
    public ShipmentStatus getStatus()     { return status; }
    public Instant getShippedAt()         { return shippedAt; }
    public Instant getEstimatedDelivery() { return estimatedDelivery; }
    public Instant getCreatedAt()         { return createdAt; }
}
