package com.smxworld.ecommerce.warehouse;

import java.util.UUID;

/** Domain event published by Warehouse when a stock reservation succeeds. Consumed by Order. */
public record StockReservedEvent(
        UUID orderId,
        boolean confirmed
) {}
