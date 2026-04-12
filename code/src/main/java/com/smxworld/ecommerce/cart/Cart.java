package com.smxworld.ecommerce.cart;

import java.math.BigDecimal;
import java.util.List;

/** DTO — public contract of the Cart module. */
public record Cart(
        String userId,
        List<CartItem> items,
        BigDecimal total
) {}
