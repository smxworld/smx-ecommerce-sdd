package com.smxworld.ecommerce.order;

import java.util.List;

/** DTO — payload to create a new order at checkout. */
public record CreateOrderRequest(
        ShippingAddress shippingAddress,
        String paymentMethod,
        List<OrderItem> items
) {
    public record ShippingAddress(
            String firstName,
            String lastName,
            String street,
            String city,
            String postalCode,
            String country
    ) {
        public String format() {
            return String.join(", ",
                    firstName + " " + lastName,
                    street,
                    city,
                    postalCode,
                    country);
        }
    }
}
