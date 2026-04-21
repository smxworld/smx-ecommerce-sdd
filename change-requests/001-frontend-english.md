---
title: "Translate frontend UI to English"
status: applied
author: ""
created-at: "2026-04-14T00:00:00.000Z"
---

# Translate frontend UI to English

## Description

The React frontend was initially developed with Italian text in labels,
placeholders, buttons, error messages, loading states, and hardcoded
strings. Since the project is published as an English-language case study,
all user-facing text must be in English for consistency with the rest of the
documentation and to make the project accessible to an international
audience.

Examples of translations applied: "Cerca prodotti..." → "Search
products...", "Tutte le categorie" → "All categories", "Prezzo min" →
"Min price", "Caricamento..." → "Loading...", "Nessun prodotto trovato." →
"No products found.", "Scopri i nostri prodotti" → "Discover our products".

## Changes

- Update `code/frontend/src/pages/HomePage.jsx` — translate search form
  labels, placeholder text, empty state messages
- Update `code/frontend/src/pages/ProductPage.jsx` — translate product
  detail labels, "Add to cart" button
- Update `code/frontend/src/pages/CartPage.jsx` — translate cart item
  labels, "Proceed to checkout" button, empty cart message
- Update `code/frontend/src/pages/CheckoutPage.jsx` — translate shipping
  form labels, "Confirm order" button
- Update `code/frontend/src/pages/OrderPage.jsx` — translate confirmation
  messages, "Continue shopping" button
- Update any reusable component in `code/frontend/src/components/` that
  contains hardcoded Italian text
- Update `product/features/frontend.md` to document that all UI copy must
  be in English