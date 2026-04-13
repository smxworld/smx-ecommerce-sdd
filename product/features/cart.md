---
title: "Cart"
status: synced
author: ""
last-modified: "2026-04-10T00:00:00.000Z"
version: "1.0"
---

# Cart

## User Stories

- As a buyer, I can add a product to the cart
- As a buyer, I can modify the quantity
- As a buyer, I can remove a product
- As a buyer, I see the updated total
- As a buyer, I find my cart again in the next session

## Behavior

- The cart module manages one cart per user identified by `userId`
- Adding a product → publishes `ProductBookedEvent` to warehouse
- Removing a product → publishes `ProductUnbookedEvent`
- The price is snapshotted at the time of addition
- The total is calculated server-side

## Pending Changes

- [ ] Anonymous cart management with merge on login

## Agent Notes

- CartApi exposes: `getCart`, `addItem`, `updateItem`, `removeItem`, `clearCart`, `getCartItems`
- Persistence with Spring Data JPA on schema `smx_cart`
- Tables: `carts` and `cart_items`
