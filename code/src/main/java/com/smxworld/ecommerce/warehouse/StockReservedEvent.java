package com.smxworld.ecommerce.warehouse;

import java.util.UUID;

/** Domain event published by Warehouse when a stock reservation succeeds. Currently emitted for downstream observers. */
public record StockReservedEvent(
        UUID orderId,
        boolean confirmed
) {}
