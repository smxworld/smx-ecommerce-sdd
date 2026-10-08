package com.smxworld.ecommerce.order;

import java.util.List;

/** DTO — payload to create a new order at checkout. */
public record CreateOrderRequest(ShippingAddress shippingAddress, String paymentMethod, List<OrderItem> items) {
    /**
     * Shipping address captured at checkout, flattened by {@link #format()} into the single string stored on the order.
     */
    public record ShippingAddress(
            String firstName, String lastName, String street, String city, String postalCode, String country) {
        public String format() {
            return String.join(", ", firstName + " " + lastName, street, city, postalCode, country);
        }
    }
}
