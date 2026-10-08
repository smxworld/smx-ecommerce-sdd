package com.smxworld.ecommerce.catalog.internal.repository;

import com.smxworld.ecommerce.catalog.internal.model.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for {@link ProductDocument}, the read model behind product search.
 *
 * <p>It is kept in sync with the relational {@code Product} by the catalog module (initial indexing, rating and
 * search-score updates). See product/features/catalog-search.md.
 */
public interface ProductElasticsearchRepository extends ElasticsearchRepository<ProductDocument, String> {}
