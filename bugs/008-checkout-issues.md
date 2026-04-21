---
title: "Cart item price defaults to 1.00 and checkout request fails"
status: resolved
author: ""
created-at: "2026-04-14T00:00:00.000Z"
---

# Cart item price defaults to 1.00 and checkout request fails

## Description

Two issues block the checkout flow.

The first issue is that when a product is added to the cart, the unit
price is stored as 1.00 instead of the actual product price. The
`CartService.addItem()` method does not fetch the current price from
`CatalogApi` and defaults to a hardcoded value. The price should be
snapshotted from the catalog at the moment of addition.

The second issue is that POST `/api/checkout` returns the error "Request
body is missing or malformed" even with a valid shipping address payload.
The `CreateOrderRequest` structure expected by the backend does not match
what the frontend sends, or the `@RequestBody` annotation is missing on
the controller method. The checkout endpoint should accept a shipping
address and resolve cart items internally from `CartApi` when line items
are not included in the request body.

## Steps to reproduce

1. Add a product to the cart — the cart shows unit price 1.00
2. Proceed to checkout, fill in the shipping address, and confirm
3. The API returns an error: "Request body is missing or malformed"

## Expected behavior

The cart stores the actual product price from the catalog. The checkout
endpoint accepts a shipping address, reads cart items from `CartApi`,
and creates the order.