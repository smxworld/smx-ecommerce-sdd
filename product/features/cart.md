---
title: "Cart"
status: synced
author: ""
last-modified: "2026-04-21T00:00:00.000Z"
version: "1.1"
---

# Cart

The cart is the transient workspace where a buyer assembles an order before
checkout. Each buyer has exactly one cart, persisted across sessions, that
tracks selected products, quantities, and a server-calculated total.

## Behavior

### Adding a product

The buyer selects a product and a quantity. The system checks that a
[[Cart]] exists for the current `userId`; if not, it creates one. It then
creates a new [[CartItem]] with the product reference, the requested
quantity, and a snapshot of the product's current price. The snapshotted
price does not change if the catalog price is updated later.

On success the system publishes a `ProductBookedEvent` containing `cartId`,
`productId`, and `quantity` so that the warehouse can reserve stock.

If the product does not exist or is unavailable, the system returns an error
and does not modify the cart.

### Modifying quantity

The buyer changes the quantity of an existing [[CartItem]]. The system
validates that the new quantity is at least 1. It updates the item and
recalculates the cart total. No event is published — the warehouse booking
remains unchanged until checkout reconciliation.

If the buyer sets the quantity to zero, the system treats it as a removal
(see below).

### Removing a product

The buyer removes a [[CartItem]] from the [[Cart]]. The system deletes the
item, recalculates the total, and publishes a `ProductUnbookedEvent`
containing `cartId`, `productId`, and the previously booked quantity so that
the warehouse can release the reservation.

### Clearing the cart

The system removes all [[CartItem]] records from the [[Cart]] and publishes
one `ProductUnbookedEvent` per item. The [[Cart]] record itself is
preserved (empty, with total zero).

### Viewing the cart

The system returns the full [[Cart]] with its list of [[CartItem]] records
and the current total. The total is always calculated server-side as the sum
of `price × quantity` for every item. No client-side total is authoritative.

### Session persistence

The cart is persisted to database and tied to `userId`. When the buyer
returns in a new session, the system loads the existing cart with all its
items and the previously snapshotted prices.

## Pending Changes

- [ ] Anonymous cart management with merge on login

## Agent Notes

- CartApi exposes: `getCart`, `addItem`, `updateItem`, `removeItem`,
  `clearCart`, `getCartItems`
- Persistence with Spring Data JPA on schema `smx_cart`
- Tables: `carts` and `cart_items`
- Price snapshot is stored on `cart_items.price` at insertion time and is
  never updated from catalog