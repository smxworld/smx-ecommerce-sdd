package com.smxworld.ecommerce.warehouse;

import java.util.UUID;

/** DTO — result of a stock reservation attempt. */
public record ReservationResult(
        UUID orderId,
        boolean successful,
        String failureReason
) {}
