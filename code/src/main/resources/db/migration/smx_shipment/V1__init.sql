CREATE SCHEMA IF NOT EXISTS smx_shipment;

CREATE TABLE smx_shipment.shipments (
    id                 UUID        PRIMARY KEY,
    order_id           UUID        NOT NULL UNIQUE,
    user_id            VARCHAR(255) NOT NULL,
    tracking_number    VARCHAR(100) NOT NULL,
    carrier            VARCHAR(100) NOT NULL,
    status             VARCHAR(50)  NOT NULL,
    shipped_at         TIMESTAMPTZ,
    estimated_delivery TIMESTAMPTZ,
    created_at         TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_shipments_order_id ON smx_shipment.shipments (order_id);
