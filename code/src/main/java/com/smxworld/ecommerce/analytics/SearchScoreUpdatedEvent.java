package com.smxworld.ecommerce.analytics;

import java.util.UUID;

/** Domain event published by Analytics when a product's search score is updated. Consumed by Catalog. */
public record SearchScoreUpdatedEvent(
        UUID productId,
        double newScore
) {}
