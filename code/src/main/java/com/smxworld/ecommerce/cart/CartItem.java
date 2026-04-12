package com.smxworld.ecommerce.cart;

import java.math.BigDecimal;
import java.util.UUID;

/** DTO — a single item in the cart. */
public record CartItem(
        UUID productId,
        String productName,
        int quantity,
        BigDecimal unitPrice
) {}
