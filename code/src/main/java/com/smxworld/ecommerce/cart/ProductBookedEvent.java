package com.smxworld.ecommerce.cart;

import java.util.UUID;

/** Domain event published by Cart when a product is added/booked. Consumed by Warehouse. */
public record ProductBookedEvent(
        UUID productId,
        String userId,
        int quantity
) {}
