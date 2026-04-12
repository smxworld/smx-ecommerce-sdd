package com.smxworld.ecommerce.order;

import java.util.UUID;

/** Domain event published by Order when an order is cancelled. Consumed by Warehouse and Notification. */
public record OrderCancelledEvent(
        UUID orderId,
        String userId,
        String reason
) {}
