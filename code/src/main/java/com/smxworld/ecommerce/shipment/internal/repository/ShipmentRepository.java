package com.smxworld.ecommerce.shipment.internal.repository;

import com.smxworld.ecommerce.shipment.internal.model.ShipmentEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for {@link ShipmentEntity}.
 *
 * <p>The lookup by order id is used both to show the shipment of an order and to avoid creating a second shipment when
 * an order confirmation event is delivered again. See product/features/warehouse-shipment.md.
 */
public interface ShipmentRepository extends JpaRepository<ShipmentEntity, UUID> {
    Optional<ShipmentEntity> findByOrderId(UUID orderId);
}
