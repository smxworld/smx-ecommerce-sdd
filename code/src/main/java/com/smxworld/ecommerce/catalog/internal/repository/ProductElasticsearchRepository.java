package com.smxworld.ecommerce.catalog.internal.repository;

import com.smxworld.ecommerce.catalog.internal.model.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductElasticsearchRepository extends ElasticsearchRepository<ProductDocument, String> {}
