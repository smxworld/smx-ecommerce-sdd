package com.smxworld.ecommerce.catalog;

import java.util.List;

/** DTO — result of a catalog search. */
public record SearchResult(
        List<ProductDetails> items,
        long totalElements,
        int totalPages
) {}
