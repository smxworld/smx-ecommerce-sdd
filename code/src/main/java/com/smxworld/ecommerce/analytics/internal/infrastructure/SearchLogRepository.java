package com.smxworld.ecommerce.analytics.internal.infrastructure;

import com.smxworld.ecommerce.analytics.internal.domain.SearchLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

interface SearchLogRepository extends JpaRepository<SearchLogEntity, UUID> {

    @Query("""
        SELECT s.queryText, COUNT(s) as cnt
        FROM SearchLogEntity s
        WHERE s.queryText IS NOT NULL
        GROUP BY s.queryText
        ORDER BY cnt DESC
        """)
    List<Object[]> findTopQueries();
}
