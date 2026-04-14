---
title: "Frontend and data issues blocking complete purchase flow"
status: resolved
author: ""
created-at: "2026-04-14T00:00:00.000Z"
---

# Frontend and data issues blocking complete purchase flow

## Bug 1: Product thumbnails show placeholder instead of real image

All products display `https://placehold.co/400x300` because the seed data 
does not include real image URLs. Either use a consistent placeholder 
service or assign realistic placeholder URLs per product category.

## Bug 2: Product names are in Italian

The seed data in `smx_catalog` has Italian product names (e.g. "Smartphone XPro", 
"Cuffie Wireless", "Giacca Invernale"). Translate all product names, 
descriptions and categories to English in the Flyway seed migration 
`V2__seed_products.sql`.

Categories must also be translated:
- "Elettronica" → "Electronics"
- "Abbigliamento" → "Clothing"  
- "Casa" → "Home"

## Bug 3: Product detail page shows NaN for price

The product detail page displays NaN instead of the price. 
The API response field name for price must be verified — 
the frontend is likely reading a field that does not exist 
or has a different name in the `/api/products/:id` response.

## Bug 4: All products show as out of stock

The seed data in `smx_warehouse` sets `quantity_reserved` equal to 
`quantity_total`, resulting in `quantity_available = 0` for all products. 
Fix the seed migration `V2__seed_stock.sql` to set `quantity_reserved = 0` 
so products are available for purchase.

## Impact

These issues block the complete purchase flow: 
search → product detail → add to cart → checkout → order confirmed.