CREATE SCHEMA IF NOT EXISTS smx_payment;

CREATE TABLE smx_payment.payments (
    id              UUID           PRIMARY KEY,
    order_id        UUID           NOT NULL UNIQUE,
    amount          NUMERIC(19, 4) NOT NULL,
    status          VARCHAR(50)    NOT NULL,
    transaction_id  VARCHAR(255),
    failure_reason  VARCHAR(255),
    processed_at    TIMESTAMPTZ    NOT NULL
);

CREATE INDEX idx_payments_order_id ON smx_payment.payments (order_id);
CREATE INDEX idx_payments_status   ON smx_payment.payments (status);
