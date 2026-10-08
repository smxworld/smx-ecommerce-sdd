package com.smxworld.ecommerce.catalog.internal.model;

import java.util.List;

/** One page of search results: the matching {@link ProductDocument}s and the total number of hits. */
public record ProductSearchPage(List<ProductDocument> products, long totalHits) {
    public ProductSearchPage {
        products = List.copyOf(products);
    }
}
