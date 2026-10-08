package com.smxworld.ecommerce.warehouse.internal.repository;

import com.smxworld.ecommerce.warehouse.internal.model.StockEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for {@link StockEntity}.
 *
 * <p>Stock is addressed by product id, which is unique. See product/features/warehouse-shipment.md.
 */
public interface StockRepository extends JpaRepository<StockEntity, UUID> {
    Optional<StockEntity> findByProductId(UUID productId);
}
