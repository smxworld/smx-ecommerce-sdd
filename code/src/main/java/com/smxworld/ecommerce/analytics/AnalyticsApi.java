package com.smxworld.ecommerce.analytics;

/**
 * Public API of the Analytics module.
 * This module has no public methods: it reacts exclusively to domain events
 * ({@code SearchPerformedEvent}) via {@code @ApplicationModuleListener},
 * and publishes {@code SearchScoreUpdatedEvent} consumed by Catalog.
 */
public interface AnalyticsApi {
    // No public methods — event-driven only.
}
