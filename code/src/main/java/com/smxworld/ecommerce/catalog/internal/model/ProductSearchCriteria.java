package com.smxworld.ecommerce.catalog.internal.model;

import java.math.BigDecimal;

/**
 * Catalog-internal search criteria handed by the service to the search repository: free text, category, price range and
 * the requested page. Any criterion left {@code null} (or blank, for text and category) is not applied.
 */
public record ProductSearchCriteria(
        String text, String category, BigDecimal minPrice, BigDecimal maxPrice, int page, int size) {}
