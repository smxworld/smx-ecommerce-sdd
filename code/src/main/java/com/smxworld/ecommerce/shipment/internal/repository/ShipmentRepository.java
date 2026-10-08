package com.smxworld.ecommerce.shipment.internal.repository;

import com.smxworld.ecommerce.shipment.internal.model.ShipmentEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<ShipmentEntity, UUID> {
    Optional<ShipmentEntity> findByOrderId(UUID orderId);
}
