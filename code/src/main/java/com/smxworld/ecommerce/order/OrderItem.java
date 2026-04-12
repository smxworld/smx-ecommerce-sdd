package com.smxworld.ecommerce.order;

import java.math.BigDecimal;
import java.util.UUID;

/** DTO — a single line item in an order. */
public record OrderItem(
        UUID productId,
        String productName,
        int quantity,
        BigDecimal unitPrice
) {}
