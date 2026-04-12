package com.smxworld.ecommerce.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/** DTO — lightweight order representation used in list views. */
public record OrderSummary(
        UUID orderId,
        String userId,
        OrderStatus status,
        BigDecimal totalAmount,
        Instant createdAt
) {}
