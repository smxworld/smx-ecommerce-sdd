---
title: "Catalogo e Ricerca"
status: synced
author: ""
last-modified: "2026-04-10T00:00:00.000Z"
version: "1.0"
---

# Catalogo e Ricerca

## User Stories

- Come acquirente, posso cercare prodotti per nome o descrizione
- Come acquirente, posso filtrare per categoria e fascia di prezzo
- Come acquirente, vedo i prodotti ordinati per rilevanza
- Come acquirente, vedo la disponibilità in stock
- Come acquirente, posso visualizzare il dettaglio di un prodotto

## Comportamento

- La ricerca usa Elasticsearch tramite il modulo catalog
- Ogni ricerca pubblica un `SearchPerformedEvent` consumato da analytics
- Il modulo analytics pubblica `SearchScoreUpdatedEvent`
- Il modulo catalog consuma `SearchScoreUpdatedEvent` per aggiornare il ranking
- Il modulo catalog consuma `ReviewCreatedEvent` per aggiornare il rating medio
- La disponibilità stock viene letta da WarehouseApi on-demand

## Agent Notes

- CatalogApi espone: `getProduct(UUID)`, `search(SearchQuery)`, `updateProductScore(UUID, double)`
- SearchQuery contiene: `q`, `category`, `minPrice`, `maxPrice`, `page`, `size`
- Il documento Elasticsearch rispecchia i campi di Product più `averageRating` e `score`
- La disponibilità stock NON va nell'indice Elasticsearch