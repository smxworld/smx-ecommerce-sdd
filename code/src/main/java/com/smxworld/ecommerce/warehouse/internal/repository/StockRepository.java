package com.smxworld.ecommerce.warehouse.internal.repository;

import com.smxworld.ecommerce.warehouse.internal.model.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StockRepository extends JpaRepository<StockEntity, UUID> {
    Optional<StockEntity> findByProductId(UUID productId);
}
