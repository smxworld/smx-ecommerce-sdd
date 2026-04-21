---
title: "Warehouse and Shipment"
status: synced
author: ""
last-modified: "2026-04-21T00:00:00.000Z"
version: "1.1"
---

# Warehouse and Shipment

The warehouse module tracks product stock and handles reservations during
the purchase flow. The shipment module manages delivery tracking after an
order is confirmed. They are grouped in a single feature because they share
the physical-goods domain, but they operate as separate modules with
independent persistence.

## Behavior — Warehouse

### Stock model

Each [[Product]] has a stock record with three quantities:
`quantity_total`, `quantity_reserved`, and a derived
`quantity_available = quantity_total - quantity_reserved`. A product is
considered available when `quantity_available > 0`.

### Cart booking

When the cart module publishes a `ProductBookedEvent`, the warehouse
consumes it and increments `quantity_reserved` for the referenced product.
When the cart publishes a `ProductUnbookedEvent`, the warehouse decrements
`quantity_reserved` by the same amount.

### Stock reservation at checkout

During checkout, the order module calls `WarehouseApi.reserveStock()`
synchronously. The warehouse evaluates whether sufficient stock is
available, updates the reserved quantities, and returns a
`ReservationResult` indicating success or failure. It also publishes a
`StockReservedEvent` or `StockReservationFailedEvent` for downstream
observers.

If the available quantity is insufficient, the reservation fails and the
stock record is not modified.

### Releasing reservations

`WarehouseApi.releaseReservation(orderId)` exists as a placeholder API but
is currently a no-op. There is no per-order reservation ledger yet, so the
system cannot reverse a specific order's reservation. This is a known
limitation.

### Operator stock management

An operator can update the total quantity of a product via
`WarehouseApi.updateStock()`. This changes `quantity_total` and
consequently `quantity_available`. Reserved quantities are not affected.

### Concurrency

Stock updates use optimistic locking (`@Version`) to prevent lost updates
when multiple concurrent requests modify the same stock record. If a
version conflict occurs, the operation fails and the caller retries.

## Behavior — Shipment

### Shipment creation

The shipment module consumes `OrderConfirmedEvent` from the order module.
On receipt, it creates a new [[Shipment]] record linked to the `orderId`
and generates a simulated tracking number (a random UUID). The shipment
module then publishes an `OrderShippedEvent` consumed by the notification
module to send a shipping confirmation email.

### Viewing a shipment

The buyer retrieves shipment information via
`ShipmentApi.getShipment(orderId)`. The system returns the [[Shipment]]
with tracking number and status. If no shipment exists for the given order,
the system returns HTTP 404.

## Agent Notes

- WarehouseApi exposes: `getStock`, `reserveStock`, `releaseReservation`,
  `updateStock`
- ShipmentApi exposes: `getShipment(UUID orderId)`
- Use `@Version` for optimistic locking on the stock table
- Shipment simulates the carrier — no real carrier API integration