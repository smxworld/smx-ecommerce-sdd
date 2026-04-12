CREATE SCHEMA IF NOT EXISTS smx_order;

CREATE TABLE smx_order.orders (
    id               UUID             PRIMARY KEY,
    user_id          VARCHAR(255)     NOT NULL,
    status           VARCHAR(50)      NOT NULL,
    total_amount     NUMERIC(19, 4)   NOT NULL,
    shipping_address TEXT,
    created_at       TIMESTAMPTZ      NOT NULL,
    updated_at       TIMESTAMPTZ      NOT NULL
);

CREATE TABLE smx_order.order_items (
    id           UUID           PRIMARY KEY,
    order_id     UUID           NOT NULL REFERENCES smx_order.orders(id) ON DELETE CASCADE,
    product_id   UUID           NOT NULL,
    product_name VARCHAR(500)   NOT NULL,
    unit_price   NUMERIC(19, 4) NOT NULL,
    quantity     INTEGER        NOT NULL CHECK (quantity > 0)
);

CREATE INDEX idx_orders_user_id        ON smx_order.orders (user_id);
CREATE INDEX idx_orders_status         ON smx_order.orders (status);
CREATE INDEX idx_order_items_order_id  ON smx_order.order_items (order_id);
CREATE INDEX idx_order_items_product   ON smx_order.order_items (product_id);
