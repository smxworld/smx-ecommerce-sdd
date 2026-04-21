---
title: "Frontend and seed data issues blocking the purchase flow"
status: resolved
author: ""
created-at: "2026-04-14T00:00:00.000Z"
---

# Frontend and seed data issues blocking the purchase flow

## Description

Four issues in the frontend and seed data prevent the complete purchase
flow (search → product detail → add to cart → checkout → order confirmed)
from working.

The first issue is that the product detail page displays NaN for the
price. The frontend reads a field name that does not match the API
response from `/api/products/:id`.

The second issue is that all products appear as out of stock. The seed
migration in `smx_warehouse` sets `quantity_reserved` equal to
`quantity_total`, resulting in `quantity_available = 0` for every product.

The third issue is that product names, descriptions, and categories in
the seed data are in Italian (e.g. "Cuffie Wireless", "Giacca Invernale",
"Elettronica"). They need to be translated to English for consistency
with the rest of the project.

The fourth issue is that product thumbnails show a generic placeholder.
The seed data does not include image URLs — all products display the
same `https://placehold.co/400x300` placeholder.

## Steps to reproduce

1. Open the frontend and navigate to any product detail page
2. The price shows as NaN
3. All products show an "out of stock" badge
4. Product names and categories are in Italian
5. All product images are identical placeholders

## Expected behavior

Product detail pages show the correct price. Products with available
stock show as in stock. All seed data text is in English. Product images
use category-appropriate placeholders or realistic URLs.