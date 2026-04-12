package com.smxworld.ecommerce.order;

import java.math.BigDecimal;
import java.util.UUID;

/** Domain event published by Order when a new order is created. Consumed by Notification. */
public record OrderCreatedEvent(
        UUID orderId,
        String userId,
        BigDecimal totalAmount
) {}
