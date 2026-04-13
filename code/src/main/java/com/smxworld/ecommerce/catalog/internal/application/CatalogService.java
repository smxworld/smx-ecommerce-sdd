package com.smxworld.ecommerce.catalog.internal.application;

import com.smxworld.ecommerce.catalog.CatalogApi;
import com.smxworld.ecommerce.catalog.SearchScoreUpdatedEvent;
import com.smxworld.ecommerce.catalog.ProductDetails;
import com.smxworld.ecommerce.catalog.SearchPerformedEvent;
import com.smxworld.ecommerce.catalog.SearchQuery;
import com.smxworld.ecommerce.catalog.SearchResult;
import com.smxworld.ecommerce.catalog.internal.domain.Product;
import com.smxworld.ecommerce.catalog.internal.infrastructure.ProductDocument;
import com.smxworld.ecommerce.catalog.internal.infrastructure.ProductElasticsearchRepository;
import com.smxworld.ecommerce.catalog.internal.infrastructure.ProductJpaRepository;
import com.smxworld.ecommerce.review.ReviewCreatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
class CatalogService implements CatalogApi {

    private final ProductJpaRepository          productRepo;
    private final ProductElasticsearchRepository esRepo;
    private final ElasticsearchOperations        esOps;
    private final ApplicationEventPublisher      events;

    CatalogService(ProductJpaRepository productRepo,
                   ProductElasticsearchRepository esRepo,
                   ElasticsearchOperations esOps,
                   ApplicationEventPublisher events) {
        this.productRepo = productRepo;
        this.esRepo      = esRepo;
        this.esOps       = esOps;
        this.events      = events;
    }

    // ─── CatalogApi ───────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public ProductDetails getProduct(UUID productId) {
        return productRepo.findById(productId)
                .map(this::toDetails)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
    }

    @Override
    public SearchResult search(SearchQuery query) {
        var pageable = PageRequest.of(query.page(), query.size());

        Query esQuery = buildQuery(query, pageable);
        SearchHits<ProductDocument> hits = esOps.search(esQuery, ProductDocument.class);

        List<ProductDetails> items = hits.stream()
                .map(SearchHit::getContent)
                .map(this::docToDetails)
                .toList();

        events.publishEvent(new SearchPerformedEvent(null, query.text(), hits.getTotalHits()));

        long total      = hits.getTotalHits();
        int  totalPages = query.size() > 0
                ? (int) Math.ceil((double) total / query.size())
                : 0;

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

    private Query buildQuery(SearchQuery query, PageRequest pageable) {
        boolean hasText     = StringUtils.hasText(query.text());
        boolean hasCategory = StringUtils.hasText(query.category());
        boolean hasMinPrice = query.minPrice() != null;
        boolean hasMaxPrice = query.maxPrice() != null;

        if (!hasText && !hasCategory && !hasMinPrice && !hasMaxPrice) {
            return new StringQuery("{\"match_all\":{}}", pageable);
        }

        Criteria criteria;
        if (hasText) {
            criteria = new Criteria("name").matches(query.text())
                    .or(new Criteria("description").matches(query.text()));
        } else {
            criteria = new Criteria();
        }

        if (hasCategory) {
            criteria = criteria.and(new Criteria("category").is(query.category()));
        }

        // Filtro per fascia di prezzo: supporta min, max o entrambi
        if (hasMinPrice && hasMaxPrice) {
            criteria = criteria.and(new Criteria("price").between(query.minPrice(), query.maxPrice()));
        } else if (hasMinPrice) {
            criteria = criteria.and(new Criteria("price").greaterThanEqual(query.minPrice()));
        } else if (hasMaxPrice) {
            criteria = criteria.and(new Criteria("price").lessThanEqual(query.maxPrice()));
        }

        return new CriteriaQuery(criteria, pageable);
    }

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
                p.getId(), p.getName(), p.getDescription(),
                p.getPrice(), p.getCategory(), p.getAverageRating(), p.getSearchScore());
    }

    private ProductDetails docToDetails(ProductDocument doc) {
        return new ProductDetails(
                UUID.fromString(doc.getId()), doc.getName(), doc.getDescription(),
                doc.getPrice(), doc.getCategory(), doc.getAverageRating(), doc.getSearchScore());
    }
}
