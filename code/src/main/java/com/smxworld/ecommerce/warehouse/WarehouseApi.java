package com.smxworld.ecommerce.warehouse;

import com.smxworld.ecommerce.order.OrderItem;

import java.util.List;
import java.util.UUID;

/**
 * Public API of the Warehouse module.
 */
public interface WarehouseApi {

    StockInfo getStock(UUID productId);

    ReservationResult reserveStock(UUID orderId, List<OrderItem> items);

    void releaseReservation(UUID orderId);

    /** Backoffice operation. */
    void updateStock(UUID productId, int quantity);
}
