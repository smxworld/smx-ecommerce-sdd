package com.smxworld.ecommerce.catalog;

import java.math.BigDecimal;

/** DTO — search parameters passed to {@link CatalogApi#search(SearchQuery)}. */
public record SearchQuery(
        String text,
        String category,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        int page,
        int size
) {}
