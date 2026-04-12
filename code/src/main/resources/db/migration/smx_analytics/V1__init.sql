CREATE SCHEMA IF NOT EXISTS smx_analytics;

CREATE TABLE smx_analytics.search_logs (
    id            UUID        PRIMARY KEY,
    user_id       VARCHAR(255),
    query_text    TEXT,
    results_count BIGINT      NOT NULL DEFAULT 0,
    searched_at   TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_search_logs_searched_at ON smx_analytics.search_logs (searched_at DESC);
CREATE INDEX idx_search_logs_query       ON smx_analytics.search_logs (query_text);
