package com.smxworld.ecommerce.order;

import java.util.List;

/** DTO — payload to create a new order at checkout. */
public record CreateOrderRequest(
        String shippingAddress,
        String paymentMethod,
        List<OrderItem> items
) {}
