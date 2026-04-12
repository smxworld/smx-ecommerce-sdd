package com.smxworld.ecommerce.order;

/** Lifecycle states of an order (state machine). */
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    PAYMENT_PENDING,
    PAYMENT_FAILED,
    PREPARING,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
