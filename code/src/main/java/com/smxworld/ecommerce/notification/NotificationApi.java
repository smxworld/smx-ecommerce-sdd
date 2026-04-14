package com.smxworld.ecommerce.notification;

/**
 * Public API of the Notification module.
 * This module has no public methods: it reacts exclusively to domain events
 * ({@code OrderConfirmedEvent}, {@code PaymentFailedEvent}, {@code OrderShippedEvent})
 * via {@code @ApplicationModuleListener}.
 */
public interface NotificationApi {
    // No public methods — event-driven only.
}
