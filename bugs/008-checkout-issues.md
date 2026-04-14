---
title: "Checkout: wrong item price and order creation fails"
status: resolved
author: ""
created-at: "2026-04-14T00:00:00.000Z"
---

# Checkout: wrong item price and order creation fails

## Bug 1: Cart item price shows as 1.00 instead of actual product price

When a product is added to the cart, the unit price is stored as 1.00 
instead of the actual product price. The price should be snapshotted 
from the catalog at the moment of adding to cart.

Investigate `CartService.addItem()` — it is likely not fetching the 
actual price from `CatalogApi` and defaulting to 1.00.

## Bug 2: POST /api/checkout returns "Request body is missing or malformed"

The checkout request fails with a misleading error message.

Request payload:
```json
{
  "shippingAddress": {
    "firstName": "Steve",
    "lastName": "Rogers",
    "street": "4 Liberty Street",
    "city": "Brooklyn",
    "postalCode": "11201",
    "country": "US"
  }
}
```

Response: `{"message":"Request body is missing or malformed"}`

Investigate `CheckoutController` and `CreateOrderRequest` — the request 
body structure expected by the backend likely does not match what the 
frontend is sending. Check if `CreateOrderRequest` expects the cart items 
to be included in the payload or reads them from `CartApi` internally.
Also verify that `@RequestBody` is present on the controller method.