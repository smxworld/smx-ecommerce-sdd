package com.smxworld.ecommerce.catalog.internal.repository;

import com.smxworld.ecommerce.catalog.internal.model.ProductDocument;
import com.smxworld.ecommerce.catalog.internal.model.ProductSearchCriteria;
import com.smxworld.ecommerce.catalog.internal.model.ProductSearchPage;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * Builds and runs the Elasticsearch product search. It is the single place where search parameters are turned into an
 * Elasticsearch query. See product/features/catalog-search.md.
 */
@Repository
public class ProductSearchRepository {

    private final ElasticsearchOperations esOps;

    ProductSearchRepository(ElasticsearchOperations esOps) {
        this.esOps = esOps;
    }

    public ProductSearchPage search(ProductSearchCriteria criteria) {
        var pageable = PageRequest.of(criteria.page(), criteria.size());
        SearchHits<ProductDocument> hits = esOps.search(buildQuery(criteria, pageable), ProductDocument.class);
        return new ProductSearchPage(hits.stream().map(SearchHit::getContent).toList(), hits.getTotalHits());
    }

    private Query buildQuery(ProductSearchCriteria query, PageRequest pageable) {
        boolean hasText = StringUtils.hasText(query.text());
        boolean hasCategory = StringUtils.hasText(query.category());
        boolean hasMinPrice = query.minPrice() != null;
        boolean hasMaxPrice = query.maxPrice() != null;

        if (!hasText && !hasCategory && !hasMinPrice && !hasMaxPrice) {
            return new StringQuery("{\"match_all\":{}}", pageable);
        }

        Criteria criteria;
        if (hasText) {
            criteria = new Criteria("name").matches(query.text()).or(new Criteria("description").matches(query.text()));
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
}
