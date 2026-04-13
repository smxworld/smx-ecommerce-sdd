package com.smxworld.ecommerce.catalog;

import java.util.UUID;

/** Event published by Analytics when a product's search score is updated. Consumed by Catalog to update its index. */
public record SearchScoreUpdatedEvent(
        UUID productId,
        double newScore
) {}
