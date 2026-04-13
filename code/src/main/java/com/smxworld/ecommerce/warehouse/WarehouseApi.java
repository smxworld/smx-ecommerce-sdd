package com.smxworld.ecommerce.warehouse;

import java.util.List;
import java.util.UUID;

/**
 * Public API of the Warehouse module.
 */
public interface WarehouseApi {

    StockInfo getStock(UUID productId);

    ReservationResult reserveStock(UUID orderId, List<ReservationItem> items);

    void releaseReservation(UUID orderId);

    /** Backoffice operation. */
    void updateStock(UUID productId, int quantity);
}
