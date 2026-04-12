package com.smxworld.ecommerce.shipment;

import java.time.Instant;
import java.util.UUID;

/** DTO — tracking information for a shipment. */
public record ShipmentInfo(
        UUID orderId,
        String trackingNumber,
        String carrier,
        ShipmentStatus status,
        Instant estimatedDelivery,
        Instant lastUpdate
) {}
