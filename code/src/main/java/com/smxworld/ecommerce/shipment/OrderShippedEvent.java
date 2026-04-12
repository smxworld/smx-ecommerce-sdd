package com.smxworld.ecommerce.shipment;

import java.util.UUID;

/** Domain event published by Shipment when an order is shipped. Consumed by Notification. */
public record OrderShippedEvent(
        UUID orderId,
        String userId,
        String trackingNumber
) {}
