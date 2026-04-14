package com.smxworld.ecommerce.catalog.internal.application;

import com.smxworld.ecommerce.catalog.internal.domain.Product;
import com.smxworld.ecommerce.catalog.internal.infrastructure.ProductDocument;
import com.smxworld.ecommerce.catalog.internal.infrastructure.ProductElasticsearchRepository;
import com.smxworld.ecommerce.catalog.internal.infrastructure.ProductJpaRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class CatalogIndexInitializer {

    private final ProductJpaRepository productRepo;
    private final ProductElasticsearchRepository esRepo;
    private final ElasticsearchOperations esOps;

    CatalogIndexInitializer(ProductJpaRepository productRepo,
                            ProductElasticsearchRepository esRepo,
                            ElasticsearchOperations esOps) {
        this.productRepo = productRepo;
        this.esRepo = esRepo;
        this.esOps = esOps;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional(readOnly = true)
    void initializeIndex() {
        var indexOps = esOps.indexOps(ProductDocument.class);
        if (!indexOps.exists()) {
            indexOps.create();
            indexOps.putMapping(indexOps.createMapping());
        }

        var documents = productRepo.findAll().stream()
                .map(this::toDocument)
                .toList();

        if (!documents.isEmpty()) {
            esRepo.saveAll(documents);
        }
    }

    private ProductDocument toDocument(Product product) {
        return new ProductDocument(
                product.getId().toString(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getAverageRating(),
                product.getSearchScore());
    }
}
