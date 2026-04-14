---
title: "Add to cart returns 400 and reviews are in Italian"
status: resolved
author: ""
created-at: "2026-04-14T00:00:00.000Z"
---

# Add to cart returns 400 and reviews are in Italian

## Bug 1: POST /api/cart/items returns 400 Bad Request

Adding a product to the cart fails with 400.

Request payload:
```json
{
  "productId": "aaaaaaaa-0002-0000-0000-000000000002",
  "quantity": 1
}
```

Response: 400 Bad Request with no details.

Investigate the CartController and CartService to understand what 
validation is failing. The payload matches the documented API in 
system/interfaces.md. Enable detailed error messages to expose 
the actual validation error.

## Bug 2: Product reviews are in Italian

The seed data in `smx_review` contains Italian text for review content.
Translate all review text to English in the Flyway seed migration 
`V2__seed_reviews.sql`.