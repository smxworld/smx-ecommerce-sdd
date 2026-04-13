---
title: "Payment"
status: synced
author: ""
last-modified: "2026-04-10T00:00:00.000Z"
version: "1.0"
---

# Payment

## User Stories

- As a buyer, I can pay by credit card
- As a buyer, I receive confirmation if the payment succeeds
- As a buyer, I am notified if the payment fails

## Behavior

- Called by order via `PaymentApi.processPayment()`
- Publishes `PaymentSucceededEvent` or `PaymentFailedEvent`
- Idempotent: if `orderId` has already been processed, returns the previous result

## Pending Changes

- [ ] Support for multiple payment methods
- [ ] Refund management

## Agent Notes

- Simulated gateway: always accepts payments with amount < 10,000€
- PaymentApi exposes only: `processPayment(UUID orderId, BigDecimal amount)`
- Idempotency guaranteed via `order_id` UNIQUE on the `payments` table
