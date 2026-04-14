---
title: "Warehouse and Shipment"
status: synced
author: ""
last-modified: "2026-04-10T00:00:00.000Z"
version: "1.0"
---

# Warehouse and Shipment

## User Stories

- As a buyer, I can see whether a product is available
- As a buyer, I receive a notification when the order is shipped
- As an operator, I can update the available quantity

## Behavior — Warehouse

- Consumes `ProductBookedEvent` and `ProductUnbookedEvent` from cart
- When called by order via `reserveStock()`: reserves stock synchronously, returns `ReservationResult`, and also publishes `StockReservedEvent` or `StockReservationFailedEvent`
- `releaseReservation(orderId)` currently exists as a placeholder API but is a no-op because there is no per-order reservation ledger yet
- `quantity_available = quantity_total - quantity_reserved`

## Behavior — Shipment

- Consumes `OrderConfirmedEvent` from the order module to register the shipment
- Generates a simulated tracking number
- Publishes `OrderShippedEvent` consumed by notification
- ShipmentApi exposes: `getShipment(UUID orderId)`

## Agent Notes

- Use `@Version` for optimistic locking on the stock table
- WarehouseApi exposes: `getStock`, `reserveStock`, `releaseReservation`, `updateStock`
- Shipment simulates the carrier by generating a UUID tracking number
