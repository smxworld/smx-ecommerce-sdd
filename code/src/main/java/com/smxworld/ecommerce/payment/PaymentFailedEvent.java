package com.smxworld.ecommerce.payment;

import java.util.UUID;

/** Domain event published by Payment on failure. Consumed by Order and Notification. */
public record PaymentFailedEvent(
        UUID orderId,
        String reason
) {}
