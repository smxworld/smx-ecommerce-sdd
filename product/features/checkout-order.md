---
title: "Checkout e Ordine"
status: synced
author: ""
last-modified: "2026-04-10T00:00:00.000Z"
version: "1.0"
---

# Checkout e Ordine

## User Stories

- Come acquirente, posso avviare il checkout dal carrello
- Come acquirente, inserisco l'indirizzo di spedizione
- Come acquirente, confermo l'ordine e ricevo una notifica
- Come acquirente, vedo lo stato del mio ordine

## Flusso di Checkout
1. POST /api/checkout → OrderApi.createOrder()
2. Order crea ordine in stato PENDING
3. Order chiama WarehouseApi.reserveStock()
4. Warehouse pubblica StockReservedEvent o StockReservationFailedEvent
5. Se stock ok → Order chiama PaymentApi.processPayment()
6. Payment pubblica PaymentSucceededEvent o PaymentFailedEvent
7. Se pagamento ok → Order va in CONFIRMED, pubblica OrderConfirmedEvent
8. Notification consuma OrderConfirmedEvent → invia email
9. Se qualcosa fallisce → Order va in CANCELLED, pubblica OrderCancelledEvent