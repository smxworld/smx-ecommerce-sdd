package com.smxworld.ecommerce.warehouse;

import java.util.UUID;

/** DTO — current stock information for a product. */
public record StockInfo(
        UUID productId,
        int availableQuantity,
        int reservedQuantity
) {}
