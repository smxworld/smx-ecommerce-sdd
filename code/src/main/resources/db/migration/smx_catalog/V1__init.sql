-- Schema e tabella prodotti del modulo catalog
CREATE SCHEMA IF NOT EXISTS smx_catalog;

CREATE TABLE smx_catalog.products (
    id             UUID             PRIMARY KEY,
    name           VARCHAR(500)     NOT NULL,
    description    TEXT,
    price          NUMERIC(19, 4)   NOT NULL,
    category       VARCHAR(255)     NOT NULL,
    average_rating DOUBLE PRECISION NOT NULL DEFAULT 0,
    review_count   INTEGER          NOT NULL DEFAULT 0,
    search_score   DOUBLE PRECISION NOT NULL DEFAULT 0,
    created_at     TIMESTAMPTZ      NOT NULL,
    updated_at     TIMESTAMPTZ      NOT NULL
);

CREATE INDEX idx_products_category ON smx_catalog.products (category);
CREATE INDEX idx_products_search_score ON smx_catalog.products (search_score DESC);
