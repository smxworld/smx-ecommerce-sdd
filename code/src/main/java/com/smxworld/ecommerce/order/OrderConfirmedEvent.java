package com.smxworld.ecommerce.order;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Domain event published by Order when an order is confirmed.
 * Consumed by Notification.
 *
 * <p>To externalize on Kafka: add {@code @Externalized("smx.order-confirmed")}.
 */
public record OrderConfirmedEvent(
        UUID orderId,
        String userId,
        BigDecimal totalAmount
) {}
