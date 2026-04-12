package com.smxworld.ecommerce.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/** DTO — full order representation including line items. */
public record OrderDetails(
        UUID orderId,
        String userId,
        OrderStatus status,
        List<OrderItem> items,
        BigDecimal totalAmount,
        Instant createdAt,
        Instant updatedAt
) {}
