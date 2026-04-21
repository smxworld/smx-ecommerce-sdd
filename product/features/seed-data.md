---
title: "Seed Data"
status: synced
author: ""
last-modified: "2026-04-21T00:00:00.000Z"
version: "1.1"
---

# Seed Data

Seed data populates the database with realistic test data so the complete
purchase flow can be tested without manual data entry. Seeds are loaded
automatically at application startup and are fully repeatable.

## Behavior

### Products

The system loads 10 products distributed across 3 categories:

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

Products are inserted via Flyway migration scripts into both `smx_catalog`
(product records) and `smx_warehouse` (stock records). All seeds use fixed
hardcoded UUIDs so they are repeatable and cross-referenceable across
scripts.

### Users

Keycloak is configured with a `smxworld` realm containing two users:

| Username | Password | Role | Purpose |
|---|---|---|---|
| `buyer@smxworld.local` | `password123` | `ROLE_USER` | Testing the purchase flow |
| `operator@smxworld.local` | `password123` | `ROLE_OPERATOR` | Back-office operations |

Users are created via `infrastructure/keycloak/smxworld-realm.json`,
imported automatically when Keycloak starts with the `--import-realm` flag.

### Reviews

5 pre-loaded reviews are inserted on Smartphone XPro and Laptop UltraSlim
via Flyway migration scripts in `smx_review`. These reviews provide sample
data for the rating system in the product detail page.

### How data is loaded

All seed migrations run as part of the normal application startup against
the configured local database. No dedicated Spring profile is required.

## Agent Notes

- Create `code/src/main/resources/db/migration/smx_catalog/V2__seed_products.sql`
- Create `code/src/main/resources/db/migration/smx_warehouse/V2__seed_stock.sql`
- Create `code/src/main/resources/db/migration/smx_review/V2__seed_reviews.sql`
- Seeds use fixed UUIDs so IDs are consistent across catalog, warehouse,
  and review scripts
- Create `infrastructure/keycloak/smxworld-realm.json` with the `smxworld`
  realm and the two users listed above
- No dedicated Spring profile is required for the current local seed setup