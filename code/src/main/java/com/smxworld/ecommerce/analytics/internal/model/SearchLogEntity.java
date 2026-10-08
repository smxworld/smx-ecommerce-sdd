package com.smxworld.ecommerce.analytics.internal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

/**
 * JPA entity for one executed product search, persisted in {@code smx_analytics.search_logs}.
 *
 * <p>Each search published by the catalog module is stored with the query, the number of results and the (optional)
 * user, to build the popularity data that feeds back into product ranking. See product/features/catalog-search.md.
 */
@Entity
@Table(name = "search_logs", schema = "smx_analytics")
public class SearchLogEntity {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "user_id", length = 255)
    private String userId;

    @Column(name = "query_text", columnDefinition = "TEXT")
    private String queryText;

    @Column(name = "results_count", nullable = false)
    private long resultsCount;

    @Column(name = "searched_at", nullable = false, updatable = false)
    private Instant searchedAt;

    protected SearchLogEntity() {}

    public SearchLogEntity(String userId, String queryText, long resultsCount) {
        this.userId = userId;
        this.queryText = queryText;
        this.resultsCount = resultsCount;
        this.searchedAt = Instant.now();
    }

    @PrePersist
    private void onCreate() {
        if (this.searchedAt == null) {
            this.searchedAt = Instant.now();
        }
    }

    public UUID getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getQueryText() {
        return queryText;
    }

    public long getResultsCount() {
        return resultsCount;
    }

    public Instant getSearchedAt() {
        return searchedAt;
    }
}
