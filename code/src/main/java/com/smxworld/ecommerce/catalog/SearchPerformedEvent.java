package com.smxworld.ecommerce.catalog;

/** Domain event published by Catalog when a search is performed. Consumed by Analytics. */
public record SearchPerformedEvent(
        String userId,
        String query,
        long resultsCount
) {}
