---
title: "Recensioni e Notifiche"
status: synced
author: ""
last-modified: "2026-04-10T00:00:00.000Z"
version: "1.0"
---

# Recensioni e Notifiche

## User Stories — Recensioni

- Come acquirente, posso lasciare una recensione (voto 1-5 + testo) su un prodotto acquistato
- Come acquirente, vedo le recensioni di altri utenti
- Come acquirente, vedo il voto medio del prodotto

## Comportamento — Review

- Accetta recensioni solo per prodotti in un ordine DELIVERED dell'utente
- Verifica tramite `OrderApi.hasDeliveredOrderWithProduct(userId, productId)`
- Pubblica `ReviewCreatedEvent` consumato da catalog per aggiornare il rating medio
- ReviewApi espone: `getReviews(UUID productId)`, `createReview(...)`

## User Stories — Notifiche

- Come acquirente, ricevo email quando l'ordine è confermato
- Come acquirente, ricevo email se il pagamento fallisce
- Come acquirente, ricevo email quando l'ordine viene spedito

## Comportamento — Notification

Consuma:
- `OrderConfirmedEvent` → email "Il tuo ordine è confermato"
- `PaymentFailedEvent` → email "Problema con il pagamento"
- `OrderShippedEvent` → email "Il tuo ordine è in viaggio"

## Agent Notes

- Il modulo notification non espone API pubblica — solo eventi
- Usare Spring Mail + Thymeleaf per i template email
- Template in `resources/templates/mail/`
- SMTP mockato con Mailhog in sviluppo