package com.smxworld.ecommerce.catalog.internal.service;

import com.smxworld.ecommerce.catalog.CatalogApi;
import com.smxworld.ecommerce.catalog.ProductDetails;
import com.smxworld.ecommerce.catalog.SearchPerformedEvent;
import com.smxworld.ecommerce.catalog.SearchQuery;
import com.smxworld.ecommerce.catalog.SearchResult;
import com.smxworld.ecommerce.catalog.SearchScoreUpdatedEvent;
import com.smxworld.ecommerce.catalog.internal.model.Product;
import com.smxworld.ecommerce.catalog.internal.model.ProductDocument;
import com.smxworld.ecommerce.catalog.internal.model.ProductSearchCriteria;
import com.smxworld.ecommerce.catalog.internal.model.ProductSearchPage;
import com.smxworld.ecommerce.catalog.internal.repository.ProductElasticsearchRepository;
import com.smxworld.ecommerce.catalog.internal.repository.ProductJpaRepository;
import com.smxworld.ecommerce.catalog.internal.repository.ProductSearchRepository;
import com.smxworld.ecommerce.review.ReviewCreatedEvent;
import java.util.List;
import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class CatalogService implements CatalogApi {

    private final ProductJpaRepository productRepo;
    private final ProductElasticsearchRepository esRepo;
    private final ProductSearchRepository searchRepo;
    private final ApplicationEventPublisher events;

    CatalogService(
            ProductJpaRepository productRepo,
            ProductElasticsearchRepository esRepo,
            ProductSearchRepository searchRepo,
            ApplicationEventPublisher events) {
        this.productRepo = productRepo;
        this.esRepo = esRepo;
        this.searchRepo = searchRepo;
        this.events = events;
    }

    // ─── CatalogApi ───────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public ProductDetails getProduct(UUID productId) {
        return productRepo
                .findById(productId)
                .map(this::toDetails)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
    }

    @Override
    public SearchResult search(SearchQuery query) {
        ProductSearchPage page = searchRepo.search(new ProductSearchCriteria(
                query.text(), query.category(), query.minPrice(), query.maxPrice(), query.page(), query.size()));

        List<ProductDetails> items =
                page.products().stream().map(this::docToDetails).toList();

        long total = page.totalHits();
        events.publishEvent(new SearchPerformedEvent(null, query.text(), total));

        int totalPages = query.size() > 0 ? (int) Math.ceil((double) total / query.size()) : 0;

        return new SearchResult(items, total, totalPages);
    }

    @Override
    public void updateProductScore(UUID productId, double score) {
        productRepo.findById(productId).ifPresent(product -> {
            product.updateSearchScore(score);
            productRepo.save(product);
            syncScoreToEs(productId, score);
        });
    }

    // ─── Event listeners ──────────────────────────────────────────────────────

    /** Aggiorna il rating medio quando viene pubblicata una nuova recensione. */
    @ApplicationModuleListener
    void on(ReviewCreatedEvent event) {
        productRepo.findById(event.productId()).ifPresent(product -> {
            product.applyNewRating(event.rating());
            productRepo.save(product);
            syncRatingToEs(event.productId(), product.getAverageRating());
        });
    }

    /** Aggiorna lo score di ricerca quando analytics pubblica un nuovo punteggio. */
    @ApplicationModuleListener
    void on(SearchScoreUpdatedEvent event) {
        updateProductScore(event.productId(), event.newScore());
    }

    // ─── Private helpers ──────────────────────────────────────────────────────

    private void syncScoreToEs(UUID productId, double score) {
        esRepo.findById(productId.toString()).ifPresent(doc -> {
            doc.setSearchScore(score);
            esRepo.save(doc);
        });
    }

    private void syncRatingToEs(UUID productId, double averageRating) {
        esRepo.findById(productId.toString()).ifPresent(doc -> {
            doc.setAverageRating(averageRating);
            esRepo.save(doc);
        });
    }

    private ProductDetails toDetails(Product p) {
        return new ProductDetails(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getCategory(),
                p.getAverageRating(),
                p.getSearchScore());
    }

    private ProductDetails docToDetails(ProductDocument doc) {
        return new ProductDetails(
                UUID.fromString(doc.getId()),
                doc.getName(),
                doc.getDescription(),
                doc.getPrice(),
                doc.getCategory(),
                doc.getAverageRating(),
                doc.getSearchScore());
    }
}
