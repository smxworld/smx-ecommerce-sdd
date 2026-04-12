CREATE SCHEMA IF NOT EXISTS smx_warehouse;

CREATE TABLE smx_warehouse.stock (
    id                UUID        PRIMARY KEY,
    product_id        UUID        NOT NULL UNIQUE,
    quantity_total    INTEGER     NOT NULL DEFAULT 0 CHECK (quantity_total >= 0),
    quantity_reserved INTEGER     NOT NULL DEFAULT 0 CHECK (quantity_reserved >= 0),
    version           INTEGER     NOT NULL DEFAULT 0,
    updated_at        TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_stock_product_id ON smx_warehouse.stock (product_id);
