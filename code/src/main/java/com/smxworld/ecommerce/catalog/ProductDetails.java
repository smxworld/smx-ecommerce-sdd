package com.smxworld.ecommerce.catalog;

import java.math.BigDecimal;
import java.util.UUID;

/** DTO — public contract of the Catalog module. */
public record ProductDetails(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        String category,
        double averageRating,
        double searchScore
) {}
