CREATE SCHEMA IF NOT EXISTS smx_cart;

CREATE TABLE smx_cart.carts (
    id         UUID        PRIMARY KEY,
    user_id    VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE smx_cart.cart_items (
    id           UUID             PRIMARY KEY,
    cart_id      UUID             NOT NULL REFERENCES smx_cart.carts(id) ON DELETE CASCADE,
    product_id   UUID             NOT NULL,
    product_name VARCHAR(500)     NOT NULL,
    quantity     INTEGER          NOT NULL CHECK (quantity > 0),
    unit_price   NUMERIC(19, 4)   NOT NULL,
    added_at     TIMESTAMPTZ      NOT NULL
);

CREATE INDEX idx_cart_items_cart_id   ON smx_cart.cart_items (cart_id);
CREATE INDEX idx_cart_items_product   ON smx_cart.cart_items (product_id);
