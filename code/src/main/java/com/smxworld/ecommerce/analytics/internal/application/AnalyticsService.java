package com.smxworld.ecommerce.analytics.internal.application;

import com.smxworld.ecommerce.analytics.AnalyticsApi;
import com.smxworld.ecommerce.analytics.internal.domain.SearchLogEntity;
import com.smxworld.ecommerce.analytics.internal.infrastructure.SearchLogRepository;
import com.smxworld.ecommerce.catalog.SearchPerformedEvent;
import com.smxworld.ecommerce.catalog.SearchScoreUpdatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
class AnalyticsService implements AnalyticsApi {

    private final SearchLogRepository logRepo;
    private final ApplicationEventPublisher events;

    AnalyticsService(SearchLogRepository logRepo, ApplicationEventPublisher events) {
        this.logRepo = logRepo;
        this.events  = events;
    }

    @ApplicationModuleListener
    void on(SearchPerformedEvent event) {
        logRepo.save(new SearchLogEntity(event.userId(), event.query(), event.resultsCount()));
        recalculateScores();
    }

    // ─── Private helpers ────────────────────────��─────────────────────────────

    /**
     * Simple score recalculation: queries that appear more often get a higher score.
     * Published as SearchScoreUpdatedEvent consumed by Catalog.
     *
     * <p>Note: In a real system this would involve product-level click-through data.
     * Here we derive a proxy score from query frequency.
     */
    private void recalculateScores() {
        List<Object[]> topQueries = logRepo.findTopQueries();
        long maxCount = topQueries.isEmpty() ? 1L : ((Number) topQueries.get(0)[1]).longValue();

        topQueries.stream()
                .limit(50)
                .forEach(row -> {
                    String query = (String) row[0];
                    long count   = ((Number) row[1]).longValue();
                    double score = (double) count / maxCount * 10.0;

                    // Emit a synthetic product score update.
                    // In a real system, we'd link queries to product IDs via click logs.
                    // Here we use a deterministic UUID derived from the query string as a placeholder.
                    UUID productId = UUID.nameUUIDFromBytes(query.getBytes());
                    events.publishEvent(new SearchScoreUpdatedEvent(productId, score));
                });
    }
}
