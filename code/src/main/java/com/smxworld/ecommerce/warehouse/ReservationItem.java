package com.smxworld.ecommerce.warehouse;

import java.util.UUID;

/**
 * DTO used by callers of {@link WarehouseApi#reserveStock} to avoid a
 * dependency on the order module's {@code OrderItem}.
 */
public record ReservationItem(UUID productId, int quantity) {}
