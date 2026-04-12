package com.smxworld.ecommerce.cart;

import java.util.UUID;

/** Domain event published by Cart when a product is removed/unbooked. Consumed by Warehouse. */
public record ProductUnbookedEvent(
        UUID productId,
        String userId,
        int quantity
) {}
