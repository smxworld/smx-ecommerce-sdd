---
title: "Users"
status: synced
author: ""
last-modified: "2026-04-10T00:00:00.000Z"
version: "1.0"
---

# Users

## Buyer

The primary user of the platform. Registers, browses the catalog, purchases products, and tracks their orders.

### Needs

- Find products quickly through search and filters
- Add products to the cart and modify it freely
- Complete the purchase in a few steps with secure payment
- Receive confirmations and updates on order status
- Leave reviews on purchased products

### Expected behavior

- Authenticates via OAuth2 login (Keycloak)
- The cart persists across sessions
- Can have multiple active orders simultaneously
- Receives email notifications for every order status change

## Back-Office Operator

Internal user with elevated privileges. Manages the warehouse, monitors orders, and updates the catalog.

### Needs

- View all orders and their current status
- Update product availability in the warehouse
- Manage the product catalog
- Manually trigger the shipment process

### Expected behavior

- Authenticates with role `ROLE_OPERATOR`
- Has direct access to dedicated administrative endpoints via REST
- Does not interact with the public frontend
