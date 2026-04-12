CREATE SCHEMA IF NOT EXISTS smx_review;

CREATE TABLE smx_review.reviews (
    id         UUID        PRIMARY KEY,
    product_id UUID        NOT NULL,
    user_id    VARCHAR(255) NOT NULL,
    order_id   UUID        NOT NULL,
    rating     INTEGER     NOT NULL CHECK (rating BETWEEN 1 AND 5),
    text       TEXT,
    created_at TIMESTAMPTZ NOT NULL,
    UNIQUE (user_id, product_id)
);

CREATE INDEX idx_reviews_product_id ON smx_review.reviews (product_id);
CREATE INDEX idx_reviews_user_id    ON smx_review.reviews (user_id);
