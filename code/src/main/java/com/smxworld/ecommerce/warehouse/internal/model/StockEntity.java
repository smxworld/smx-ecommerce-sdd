package com.smxworld.ecommerce.warehouse.internal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

/**
 * JPA entity for the stock of a product, persisted in {@code smx_warehouse.stock}.
 *
 * <p>Available quantity is derived as total minus reserved. Reservation, release and booking are methods of the entity
 * so the invariant (never reserve more than available) lives in one place; the {@code @Version} field provides
 * optimistic locking so that concurrent checkouts cannot overwrite each other's updates. See
 * product/features/warehouse-shipment.md.
 */
@Entity
@Table(name = "stock", schema = "smx_warehouse")
public class StockEntity {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "product_id", nullable = false, unique = true)
    private UUID productId;

    @Column(name = "quantity_total", nullable = false)
    private int quantityTotal;

    @Column(name = "quantity_reserved", nullable = false)
    private int quantityReserved;

    @Version
    @Column(nullable = false)
    private int version;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected StockEntity() {}

    public StockEntity(UUID productId, int quantityTotal) {
        this.productId = productId;
        this.quantityTotal = quantityTotal;
        this.quantityReserved = 0;
        this.updatedAt = Instant.now();
    }

    @PrePersist
    @PreUpdate
    private void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public int availableQuantity() {
        return quantityTotal - quantityReserved;
    }

    public boolean reserve(int qty) {
        if (availableQuantity() < qty) {
            return false;
        }
        this.quantityReserved += qty;
        return true;
    }

    public void release(int qty) {
        this.quantityReserved = Math.max(0, this.quantityReserved - qty);
    }

    public void adjustTotal(int newTotal) {
        this.quantityTotal = newTotal;
    }

    public void book(int qty) {
        this.quantityReserved = Math.max(0, this.quantityReserved - qty);
        this.quantityTotal = Math.max(0, this.quantityTotal - qty);
    }

    public UUID getId() {
        return id;
    }

    public UUID getProductId() {
        return productId;
    }

    public int getQuantityTotal() {
        return quantityTotal;
    }

    public int getQuantityReserved() {
        return quantityReserved;
    }

    public int getVersion() {
        return version;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
