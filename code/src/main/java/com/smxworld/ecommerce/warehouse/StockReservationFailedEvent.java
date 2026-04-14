package com.smxworld.ecommerce.warehouse;

import java.util.UUID;

/** Domain event published by Warehouse when a stock reservation fails. Currently emitted for downstream observers. */
public record StockReservationFailedEvent(
        UUID orderId,
        String reason
) {}
