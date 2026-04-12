package com.smxworld.ecommerce.catalog.internal.infrastructure;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductElasticsearchRepository extends ElasticsearchRepository<ProductDocument, String> {}
