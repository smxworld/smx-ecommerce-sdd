package com.smxworld.ecommerce.order;

import java.math.BigDecimal;
import java.util.UUID;

/** Domain event published by Order when a new order is created. Currently emitted for downstream observers. */
public record OrderCreatedEvent(
        UUID orderId,
        String userId,
        BigDecimal totalAmount
) {}
