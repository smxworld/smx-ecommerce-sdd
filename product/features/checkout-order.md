---
title: "Checkout and Order"
status: synced
author: ""
last-modified: "2026-04-10T00:00:00.000Z"
version: "1.0"
---

# Checkout and Order

## User Stories

- As a buyer, I can start the checkout from the cart
- As a buyer, I enter the shipping address
- As a buyer, I confirm the order and receive a notification
- As a buyer, I can see the status of my order

## Checkout Flow
1. POST /api/checkout → OrderApi.createOrder()
2. Order creates the order in PENDING state
3. Order resolves line items from the request or, if omitted, from the current cart
4. Order calls `WarehouseApi.reserveStock()` and evaluates the returned `ReservationResult`
5. Warehouse also publishes `StockReservedEvent` or `StockReservationFailedEvent` for downstream observers
6. If stock is available → Order calls `PaymentApi.processPayment()`
7. Payment returns `PaymentResult` synchronously and also publishes `PaymentSucceededEvent` or `PaymentFailedEvent`
8. If payment succeeds → Order goes to CONFIRMED, clears the cart, and publishes `OrderConfirmedEvent`
9. Shipment consumes `OrderConfirmedEvent` and creates a shipment; notification sends confirmation and shipment emails
10. If something fails → Order goes to CANCELLED and publishes `OrderCancelledEvent`
