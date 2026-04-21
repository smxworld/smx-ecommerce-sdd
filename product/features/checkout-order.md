---
title: "Checkout and Order"
status: synced
author: ""
last-modified: "2026-04-21T00:00:00.000Z"
version: "1.1"
---

# Checkout and Order

The checkout converts a cart into a confirmed order through a synchronous
orchestration flow: stock reservation, payment processing, and order
confirmation. The order module owns the entire lifecycle from creation to
final state.

## Behavior

### Starting the checkout

The buyer submits a POST to `/api/checkout` with a shipping address. The
system calls `OrderApi.createOrder()`, which creates a new [[Order]] in
`PENDING` state. The order resolves its line items from the request body;
if line items are omitted, the system reads them from the buyer's current
[[Cart]].

### Stock reservation

The order module calls `WarehouseApi.reserveStock()` synchronously for all
line items. The warehouse evaluates availability, updates reserved
quantities, and returns a `ReservationResult` indicating success or
failure. The warehouse also publishes a `StockReservedEvent` or
`StockReservationFailedEvent` for downstream observers.

If the reservation fails (insufficient stock), the [[Order]] transitions to
`CANCELLED`, the system publishes an `OrderCancelledEvent`, and the flow
stops. No payment is attempted.

### Payment processing

If stock is reserved, the order module calls
`PaymentApi.processPayment(orderId, amount)` synchronously. The payment
module processes the charge and returns a `PaymentResult`. It also publishes
a `PaymentSucceededEvent` or `PaymentFailedEvent` for downstream observers.

If the payment fails, the [[Order]] transitions to `CANCELLED` and the
system publishes an `OrderCancelledEvent`. The reserved stock should be
released, but `releaseReservation()` is currently a no-op (see Warehouse
and Shipment).

### Order confirmation

If payment succeeds, the [[Order]] transitions to `CONFIRMED`. The system
clears the buyer's [[Cart]] and publishes an `OrderConfirmedEvent`. This
event triggers two downstream actions: the shipment module creates a
shipment record, and the notification module sends a confirmation email.

### Viewing order status

The buyer requests the [[Order]] by its UUID via GET `/api/orders/:orderId`.
The system returns the order with its current status, line items, total, and
shipping address. The buyer can only view their own orders.

## Agent Notes

- The checkout is a synchronous orchestration — no saga, no eventual
  consistency. The order module calls warehouse and payment in sequence
  and decides the final state based on their responses.
- OrderApi exposes: `createOrder(CheckoutRequest)`, `getOrder(UUID)`
- The [[Order]] status lifecycle is: `PENDING` → `CONFIRMED` or `CANCELLED`