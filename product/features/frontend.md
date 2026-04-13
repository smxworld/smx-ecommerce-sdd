---
title: "Frontend React"
status: synced
author: ""
last-modified: "2026-04-12T00:00:00.000Z"
version: "1.0"
---

# Frontend React

Applicazione React che implementa il flusso completo di acquisto: ricerca prodotto → dettaglio → carrello → checkout → conferma ordine.

## Stack Frontend

- **React 18** con Vite come build tool
- **React Router v6** per la navigazione
- **React Query (TanStack Query)** per le chiamate API e il caching
- **Tailwind CSS** per lo styling
- **Axios** per le chiamate HTTP
- **Keycloak JS** per l'autenticazione

Il frontend sta in `code/frontend/` ed è un progetto Vite separato dal backend Spring Boot.

## Schermate

### 1. Home / Ricerca (`/`)

- Barra di ricerca testuale centrata nella pagina
- Filtri: categoria (select) e fascia di prezzo (min/max)
- Griglia di prodotti (3 colonne su desktop, 1 su mobile)
- Ogni card prodotto mostra: immagine placeholder, nome, prezzo, rating medio (stelline), disponibilità stock (badge verde/rosso)
- Paginazione in fondo

### 2. Dettaglio Prodotto (`/products/:id`)

- Immagine prodotto a sinistra, info a destra
- Nome, categoria, prezzo, rating medio
- Badge disponibilità stock
- Descrizione estesa
- Input quantità + bottone "Aggiungi al carrello"
- Sezione recensioni in fondo (lista con rating e testo)

### 3. Carrello (`/cart`)

- Lista degli item nel carrello
- Ogni riga: nome prodotto, prezzo unitario, input quantità modificabile, subtotale, bottone rimuovi
- Totale in fondo a destra
- Bottone "Procedi al checkout"
- Se carrello vuoto: messaggio + link alla home

### 4. Checkout (`/checkout`)

- Form indirizzo di spedizione: nome, cognome, via, città, CAP, paese
- Riepilogo ordine (readonly): lista item + totale
- Bottone "Conferma ordine"
- Redirect alla pagina di conferma dopo il POST /api/checkout

### 5. Conferma Ordine (`/orders/:orderId`)

- Messaggio di conferma con icona di successo
- Numero ordine
- Stato ordine (badge)
- Lista item ordinati
- Bottone "Continua a fare acquisti" → torna alla home

## Autenticazione

- Al caricamento dell'app, Keycloak JS verifica se l'utente è autenticato
- Se non autenticato, redirect alla login page di Keycloak
- Il token JWT viene allegato automaticamente a tutte le chiamate API tramite un Axios interceptor
- Il token viene refreshato automaticamente prima della scadenza

## Chiamate API

Tutte le chiamate vanno verso `http://localhost:8080/api` (il backend Spring Boot).

| Schermata | Metodo | Endpoint |
|---|---|---|
| Home/Search | GET | `/api/search?q=&category=&minPrice=&maxPrice=&page=&size=` |
| Dettaglio | GET | `/api/products/:id` |
| Carrello | GET | `/api/cart` |
| Aggiungi item | POST | `/api/cart/items` |
| Modifica quantità | PUT | `/api/cart/items/:productId` |
| Rimuovi item | DELETE | `/api/cart/items/:productId` |
| Checkout | POST | `/api/checkout` |
| Stato ordine | GET | `/api/orders/:orderId` |
| Recensioni | GET | `/api/reviews/:productId` |

## Agent Notes

- Il progetto frontend va in `code/frontend/` — separato da `code/src/` del backend
- Inizializzare con `npm create vite@latest frontend -- --template react`
- Configurare il proxy Vite in `vite.config.js` per redirigere `/api` verso `http://localhost:8080`
- La struttura delle cartelle:
  ```
  code/frontend/
    src/
      components/     ← componenti riutilizzabili (ProductCard, CartItem, ecc.)
      pages/          ← schermate (HomePage, ProductPage, CartPage, CheckoutPage, OrderPage)
      hooks/          ← custom hooks React Query
      api/            ← funzioni Axios per ogni endpoint
      auth/           ← configurazione Keycloak
    public/
    index.html
    vite.config.js
    package.json
  ```
- Usare immagini placeholder da `https://placehold.co/400x300` per i prodotti
- Le stelline del rating: implementare con caratteri Unicode ★/☆ senza librerie esterne
- Nessun componente UI library (no MUI, no Ant Design) — solo Tailwind
- CORS già configurato nel backend Spring Boot per `http://localhost:5173`
