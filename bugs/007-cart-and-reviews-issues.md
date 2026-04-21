---
title: "Add to cart returns 400 and seed reviews are in Italian"
status: resolved
author: ""
created-at: "2026-04-14T00:00:00.000Z"
---

# Add to cart returns 400 and seed reviews are in Italian

## Description

Two issues found during purchase flow testing.

The first issue is that POST `/api/cart/items` returns 400 Bad Request
with no detail message. The request payload matches the documented API
contract in `system/interfaces.md` (`productId` as UUID, `quantity` as
integer), but the backend rejects it. The actual validation error is not
exposed, making diagnosis difficult without inspecting the controller
and service code.

The second issue is that the seed reviews in `smx_review` contain Italian
text. The review content needs to be translated to English in the Flyway
migration `V2__seed_reviews.sql` for consistency with the rest of the
project.

## Steps to reproduce

1. Log in as `buyer@smxworld.local`
2. Navigate to any product detail page
3. Click "Add to cart"
4. The API returns 400 Bad Request
5. Navigate to a product with reviews — review text is in Italian

## Expected behavior

Adding a product to the cart succeeds and returns the updated cart. Seed
review content is in English.