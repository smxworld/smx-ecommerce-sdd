package com.smxworld.ecommerce.order.internal.infrastructure;

import com.smxworld.ecommerce.order.OrderStatus;
import com.smxworld.ecommerce.order.internal.domain.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    List<OrderEntity> findByUserIdOrderByCreatedAtDesc(String userId);

    @Query("""
        SELECT COUNT(o) > 0
        FROM OrderEntity o
        JOIN o.items i
        WHERE o.userId = :userId
          AND i.productId = :productId
          AND o.status = :status
        """)
    boolean existsByUserIdAndItemProductIdAndStatus(String userId, UUID productId, OrderStatus status);
}
