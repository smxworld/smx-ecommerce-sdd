package com.smxworld.ecommerce.order.internal.repository;

import com.smxworld.ecommerce.order.OrderStatus;
import com.smxworld.ecommerce.order.internal.model.OrderEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data repository for {@link OrderEntity}, owned by the order module.
 *
 * <p>Besides the per-buyer order history (newest first), it exposes the existence query behind the review rule "only
 * buyers who received a product may review it": the review module reaches it through {@code OrderApi}, never directly.
 * See product/features/checkout-order.md and product/features/reviews-notification.md.
 */
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    List<OrderEntity> findByUserIdOrderByCreatedAtDesc(String userId);

    @Query("SELECT COUNT(o) > 0 FROM OrderEntity o JOIN o.items i "
            + "WHERE o.userId = :userId AND i.productId = :productId AND o.status = :status")
    boolean existsByUserIdAndItemProductIdAndStatus(String userId, UUID productId, OrderStatus status);
}
