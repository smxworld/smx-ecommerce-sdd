package com.smxworld.ecommerce.shipment;

import java.util.UUID;

/**
 * Public API of the Shipment module.
 */
public interface ShipmentApi {

    ShipmentInfo getShipment(UUID orderId);
}
