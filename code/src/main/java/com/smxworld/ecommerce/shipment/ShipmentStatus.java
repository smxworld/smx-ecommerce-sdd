package com.smxworld.ecommerce.shipment;

/** Lifecycle states of a shipment. */
public enum ShipmentStatus {
    PENDING,
    PICKED_UP,
    IN_TRANSIT,
    OUT_FOR_DELIVERY,
    DELIVERED,
    FAILED
}
