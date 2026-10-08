package com.smxworld.ecommerce.analytics.internal.repository;

import com.smxworld.ecommerce.analytics.internal.model.SearchLogEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data repository for {@link SearchLogEntity}.
 *
 * <p>It aggregates the logged searches into the most frequent queries, which feed the search score of products. See
 * product/features/catalog-search.md.
 */
public interface SearchLogRepository extends JpaRepository<SearchLogEntity, UUID> {

    @Query("SELECT s.queryText, COUNT(s) as cnt FROM SearchLogEntity s "
            + "WHERE s.queryText IS NOT NULL GROUP BY s.queryText ORDER BY cnt DESC")
    List<Object[]> findTopQueries();
}
