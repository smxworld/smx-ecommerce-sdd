package com.smxworld.ecommerce.order;

import java.util.List;
import java.util.UUID;

/**
 * Public API of the Order module.
 */
public interface OrderApi {

    OrderSummary createOrder(String userId, CreateOrderRequest request);

    OrderDetails getOrder(UUID orderId);

    List<OrderSummary> getOrdersByUser(String userId);

    /** Called by the Review module to check if the user may review a product. */
    boolean hasDeliveredOrderWithProduct(String userId, UUID productId);

    /** Backoffice operation. */
    void updateStatus(UUID orderId, OrderStatus status);
}
