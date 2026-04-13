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
3. Order calls WarehouseApi.reserveStock()
4. Warehouse publishes StockReservedEvent or StockReservationFailedEvent
5. If stock ok → Order calls PaymentApi.processPayment()
6. Payment publishes PaymentSucceededEvent or PaymentFailedEvent
7. If payment ok → Order goes to CONFIRMED, publishes OrderConfirmedEvent
8. Notification consumes OrderConfirmedEvent → sends email
9. If something fails → Order goes to CANCELLED, publishes OrderCancelledEvent
