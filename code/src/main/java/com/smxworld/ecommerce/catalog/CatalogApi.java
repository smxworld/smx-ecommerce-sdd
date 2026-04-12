package com.smxworld.ecommerce.catalog;

import java.util.UUID;

/**
 * Public API of the Catalog module.
 * Exposes product lookup and search capabilities.
 */
public interface CatalogApi {

    ProductDetails getProduct(UUID productId);

    SearchResult search(SearchQuery query);

    /** Called by the Analytics module when a product's search score changes. */
    void updateProductScore(UUID productId, double score);
}
