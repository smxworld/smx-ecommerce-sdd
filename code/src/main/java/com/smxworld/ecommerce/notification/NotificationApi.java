package com.smxworld.ecommerce.notification;

/**
 * Public API of the Notification module.
 * This module has no public methods: it reacts exclusively to domain events
 * ({@code OrderCreatedEvent}, {@code OrderConfirmedEvent}, {@code OrderCancelledEvent},
 * {@code PaymentFailedEvent}, {@code OrderShippedEvent}) via {@code @ApplicationModuleListener}.
 */
public interface NotificationApi {
    // No public methods — event-driven only.
}
