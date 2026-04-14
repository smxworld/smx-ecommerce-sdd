package com.smxworld.ecommerce.order;

import java.util.UUID;

/** Domain event published by Order when an order is cancelled. Currently emitted for downstream observers. */
public record OrderCancelledEvent(
        UUID orderId,
        String userId,
        String reason
) {}
