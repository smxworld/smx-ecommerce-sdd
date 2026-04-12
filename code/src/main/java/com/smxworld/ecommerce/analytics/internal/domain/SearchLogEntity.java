package com.smxworld.ecommerce.analytics.internal.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

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
        this.userId       = userId;
        this.queryText    = queryText;
        this.resultsCount = resultsCount;
        this.searchedAt   = Instant.now();
    }

    @PrePersist
    private void onCreate() {
        if (this.searchedAt == null) this.searchedAt = Instant.now();
    }

    public UUID getId()            { return id; }
    public String getUserId()      { return userId; }
    public String getQueryText()   { return queryText; }
    public long getResultsCount()  { return resultsCount; }
    public Instant getSearchedAt() { return searchedAt; }
}
