---
title: "Dati di Seed"
status: synced
author: ""
last-modified: "2026-04-12T00:00:00.000Z"
version: "1.0"
---

# Dati di Seed

Script SQL e configurazione per popolare il database con dati di test realistici. I dati di seed permettono di testare il flusso completo senza dover inserire dati manualmente.

## Prodotti

10 prodotti distribuiti in 3 categorie: Elettronica, Abbigliamento, Casa.

| Nome | Categoria | Prezzo | Stock |
|---|---|---|---|
| Smartphone XPro | Elettronica | 699.00 | 50 |
| Laptop UltraSlim | Elettronica | 1299.00 | 20 |
| Cuffie Wireless | Elettronica | 149.00 | 100 |
| Tastiera Meccanica | Elettronica | 89.00 | 75 |
| T-Shirt Premium | Abbigliamento | 29.00 | 200 |
| Jeans Slim Fit | Abbigliamento | 59.00 | 150 |
| Giacca Invernale | Abbigliamento | 199.00 | 40 |
| Lampada da Tavolo | Casa | 49.00 | 80 |
| Set Cuscini | Casa | 39.00 | 120 |
| Tappeto Moderno | Casa | 129.00 | 30 |

## Utenti Keycloak

| Username | Password | Ruolo | Descrizione |
|---|---|---|---|
| `acquirente@smx.local` | `password123` | `ROLE_USER` | Utente normale per testare il flusso di acquisto |
| `operatore@smx.local` | `password123` | `ROLE_OPERATOR` | Operatore backoffice |

## Recensioni di esempio

5 recensioni pre-caricate su Smartphone XPro e Laptop UltraSlim per mostrare il sistema di rating nella UI.

## Come vengono caricati i dati

- I prodotti vengono inseriti tramite script Flyway in `smx_catalog` e `smx_warehouse`
- Gli utenti Keycloak vengono creati tramite il realm export in `infrastructure/keycloak/`
- Le recensioni vengono inserite tramite script Flyway in `smx_review`
- I dati di seed sono attivi solo con il profilo Spring `dev`

## Agent Notes

- Creare `code/src/main/resources/db/migration/smx_catalog/V2__seed_products.sql`
- Creare `code/src/main/resources/db/migration/smx_warehouse/V2__seed_stock.sql`
- Creare `code/src/main/resources/db/migration/smx_review/V2__seed_reviews.sql`
- I seed usano UUID fissi (hardcoded) così sono ripetibili e referenziabili tra script
- Creare `infrastructure/keycloak/realm-export.json` con il realm `smx` configurato con i due utenti
- Il profilo dev si attiva con `SPRING_PROFILES_ACTIVE=dev` nel docker-compose.yml
