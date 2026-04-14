---
title: "Translate frontend UI to English"
status: applied
author: ""
created-at: "2026-04-14T00:00:00.000Z"
---

# Translate frontend UI to English

## Description

All user-facing text in the React frontend must be in English.
This includes labels, placeholders, buttons, error messages, 
loading states, and any hardcoded strings in components and pages.

## Scope

- `code/frontend/src/pages/HomePage.jsx` — search form, labels, messages
- `code/frontend/src/pages/ProductPage.jsx` — product details, add to cart
- `code/frontend/src/pages/CartPage.jsx` — cart items, checkout button
- `code/frontend/src/pages/CheckoutPage.jsx` — form labels, confirm button
- `code/frontend/src/pages/OrderPage.jsx` — confirmation messages
- Any other component with hardcoded Italian text

## Examples

- "Cerca prodotti..." → "Search products..."
- "Tutte le categorie" → "All categories"
- "Prezzo min" → "Min price"
- "Cerca" → "Search"
- "Caricamento..." → "Loading..."
- "Nessun prodotto trovato." → "No products found."
- "Scopri i nostri prodotti" → "Discover our products"