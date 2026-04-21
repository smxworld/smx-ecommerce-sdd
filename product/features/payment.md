---
title: "Payment"
status: synced
author: ""
last-modified: "2026-04-21T00:00:00.000Z"
version: "1.1"
---

# Payment

The payment module processes charges for confirmed orders. It is called
synchronously by the order module during checkout and guarantees
idempotency per order.

## Behavior

### Processing a payment

The order module calls `PaymentApi.processPayment(orderId, amount)`. The
system creates a new [[Payment]] record linked to the `orderId`, processes
the charge through the payment gateway, and returns a `PaymentResult`
indicating success or failure.

On success the system publishes a `PaymentSucceededEvent` containing
`orderId` and `amount`. On failure the system publishes a
`PaymentFailedEvent` with the same fields plus a failure reason. The
notification module consumes `PaymentFailedEvent` to alert the buyer.

### Idempotency

If a payment for the same `orderId` has already been processed, the system
returns the previous result without creating a new charge. This is enforced
by a UNIQUE constraint on `order_id` in the `payments` table. Duplicate
calls are safe and produce the same response.

### Simulated gateway

The current implementation uses a simulated gateway: any payment with an
amount strictly less than 10,000 € is accepted; amounts equal to or above
10,000 € are rejected. This behavior is a placeholder for a real gateway
integration.

## Pending Changes

- [ ] Support for multiple payment methods
- [ ] Refund management

## Agent Notes

- PaymentApi exposes only: `processPayment(UUID orderId, BigDecimal amount)`
- Idempotency guaranteed via `order_id` UNIQUE on the `payments` table
- The simulated gateway is implemented in-process — no external service call