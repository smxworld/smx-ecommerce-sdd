package com.smxworld.ecommerce.catalog.internal.repository;

import com.smxworld.ecommerce.catalog.internal.model.Product;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link Product}, the system of record of the catalog.
 *
 * <p>The Elasticsearch copy used for search is handled separately by {@code ProductElasticsearchRepository}. See
 * product/features/catalog-search.md.
 */
public interface ProductJpaRepository extends JpaRepository<Product, UUID> {}
