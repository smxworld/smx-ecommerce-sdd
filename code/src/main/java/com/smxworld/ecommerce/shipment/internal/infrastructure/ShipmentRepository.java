package com.smxworld.ecommerce.shipment.internal.infrastructure;

import com.smxworld.ecommerce.shipment.internal.domain.ShipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShipmentRepository extends JpaRepository<ShipmentEntity, UUID> {
    Optional<ShipmentEntity> findByOrderId(UUID orderId);
}
