---
title: "Magazzino e Spedizione"
status: synced
author: ""
last-modified: "2026-04-10T00:00:00.000Z"
version: "1.0"
---

# Magazzino e Spedizione

## User Stories

- Come acquirente, vedo se un prodotto è disponibile
- Come acquirente, ricevo una notifica quando l'ordine viene spedito
- Come operatore, posso aggiornare la quantità disponibile
- Come operatore, posso avviare manualmente la spedizione

## Comportamento — Warehouse

- Consuma `ProductBookedEvent` e `ProductUnbookedEvent` da cart
- Quando chiamato da order tramite `reserveStock()`: scala lo stock e pubblica `StockReservedEvent` o `StockReservationFailedEvent`
- Consuma `OrderCancelledEvent` per rilasciare prenotazioni
- `quantity_available = quantity_total - quantity_reserved`

## Comportamento — Shipment

- Consuma eventi da warehouse per registrare la spedizione
- Genera tracking number simulato
- Pubblica `OrderShippedEvent` consumato da notification
- ShipmentApi espone: `getShipment(UUID orderId)`

## Agent Notes

- Usare `@Version` per optimistic locking sulla tabella stock
- WarehouseApi espone: `getStock`, `reserveStock`, `releaseReservation`, `updateStock`
- Shipment simula il corriere generando un tracking number UUID