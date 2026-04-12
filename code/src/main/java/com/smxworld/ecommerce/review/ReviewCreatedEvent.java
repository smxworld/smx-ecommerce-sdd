package com.smxworld.ecommerce.review;

import java.util.UUID;

/** Domain event published by Review when a new review is created. Consumed by Catalog. */
public record ReviewCreatedEvent(
        UUID productId,
        int rating
) {}
