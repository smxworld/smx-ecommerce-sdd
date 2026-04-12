---
title: "Carrello"
status: synced
author: ""
last-modified: "2026-04-10T00:00:00.000Z"
version: "1.0"
---

# Carrello

## User Stories

- Come acquirente, posso aggiungere un prodotto al carrello
- Come acquirente, posso modificare la quantità
- Come acquirente, posso rimuovere un prodotto
- Come acquirente, vedo il totale aggiornato
- Come acquirente, ritrovo il mio carrello alla prossima sessione

## Comportamento

- Il modulo cart gestisce un carrello per utente identificato da `userId`
- Aggiunta prodotto → pubblica `ProductBookedEvent` verso warehouse
- Rimozione prodotto → pubblica `ProductUnbookedEvent`
- Il prezzo viene snapshotted al momento dell'aggiunta
- Il totale è calcolato lato server

## Pending Changes

- [ ] Gestione carrello anonimo con merge al login

## Agent Notes

- CartApi espone: `getCart`, `addItem`, `updateItem`, `removeItem`, `clearCart`, `getCartItems`
- Persistenza con Spring Data JPA su schema `smx_cart`
- Tabelle: `carts` e `cart_items`