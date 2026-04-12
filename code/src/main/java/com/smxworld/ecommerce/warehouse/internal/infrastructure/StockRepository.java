package com.smxworld.ecommerce.warehouse.internal.infrastructure;

import com.smxworld.ecommerce.warehouse.internal.domain.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface StockRepository extends JpaRepository<StockEntity, UUID> {
    Optional<StockEntity> findByProductId(UUID productId);
}
