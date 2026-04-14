---
title: "Seed Data"
status: synced
author: ""
last-modified: "2026-04-12T00:00:00.000Z"
version: "1.0"
---

# Seed Data

SQL scripts and configuration to populate the database with realistic test data. Seed data allows testing the complete flow without having to enter data manually.

## Products

10 products distributed across 3 categories: Electronics, Clothing, Home.

| Name | Category | Price | Stock |
|---|---|---|---|
| Smartphone XPro | Electronics | 699.00 | 50 |
| Laptop UltraSlim | Electronics | 1299.00 | 20 |
| Wireless Headphones | Electronics | 149.00 | 100 |
| Mechanical Keyboard | Electronics | 89.00 | 75 |
| Premium T-Shirt | Clothing | 29.00 | 200 |
| Slim Fit Jeans | Clothing | 59.00 | 150 |
| Winter Jacket | Clothing | 199.00 | 40 |
| Desk Lamp | Home | 49.00 | 80 |
| Cushion Set | Home | 39.00 | 120 |
| Modern Rug | Home | 129.00 | 30 |

## Keycloak Users

| Username | Password | Role | Description |
|---|---|---|---|
| `buyer@smxworld.local` | `password123` | `ROLE_USER` | Standard user for testing the purchase flow |
| `operator@smxworld.local` | `password123` | `ROLE_OPERATOR` | Back-office operator |

## Sample Reviews

5 pre-loaded reviews on Smartphone XPro and Laptop UltraSlim to showcase the rating system in the UI.

## How data is loaded

- Products are inserted via Flyway scripts in `smx_catalog` and `smx_warehouse`
- Keycloak users are created via `infrastructure/keycloak/smxworld-realm.json`, imported automatically when Keycloak starts with `--import-realm`
- Reviews are inserted via Flyway scripts in `smx_review`
- Seed migrations run as part of the normal application startup against the configured local database

## Agent Notes

- Create `code/src/main/resources/db/migration/smx_catalog/V2__seed_products.sql`
- Create `code/src/main/resources/db/migration/smx_warehouse/V2__seed_stock.sql`
- Create `code/src/main/resources/db/migration/smx_review/V2__seed_reviews.sql`
- Seeds use fixed (hardcoded) UUIDs so they are repeatable and cross-referenceable between scripts
- Create `infrastructure/keycloak/smxworld-realm.json` with the `smxworld` realm configured with the two users
- No dedicated Spring profile is required for the current local seed setup
