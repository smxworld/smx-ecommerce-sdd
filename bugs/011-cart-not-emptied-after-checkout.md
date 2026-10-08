---
title: "Cart is not emptied after checkout"
status: resolved
author: ""
created-at: "2026-10-08T09:46:00.000Z"
---

# Cart is not emptied after checkout

## Description

After a buyer completes checkout and the order is created, the buyer's [[Cart]] still contains its items. The cart is expected to be empty once the order exists.

The problem was observed in the running application (backend started with `mvn spring-boot:run`, infrastructure started with `docker compose up -d`) on the branch `chore/quality-gates`. That branch changed how the cart exposes its items: `CartEntity.getItems()` returns an unmodifiable view, a `clearItems()` method was added to `CartEntity`, and `CartService` no longer clears the list from outside the entity (commit `fix: resolve SpotBugs findings`). It is not known whether the problem existed before these changes. The last commit before them is `4aa4305`.

The automated test suite passes, so no existing test covers this behavior against a real database.

## Steps to reproduce

1. Start the infrastructure with `docker compose up -d` and the application with `mvn spring-boot:run` from the `code/` directory.
2. As a buyer, add at least one product to the cart.
3. Read the cart and confirm it contains the product.
4. Complete the checkout so that an [[Order]] is created.
5. Read the cart again.

Observed result: the cart still contains the items added in step 2.

## Expected behavior

When the checkout completes and the [[Order]] is created, the buyer's [[Cart]] is emptied. Reading the cart afterwards returns no items, including after the application is restarted. The [[Order]] keeps its own order lines, unaffected by the emptying of the cart.
