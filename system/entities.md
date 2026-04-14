---
title: "Data Entities"
status: synced
author: ""
last-modified: "2026-04-10T00:00:00.000Z"
version: "1.0"
---

# Data Entities

Main entities of the system, grouped by owning module. Each entity lives in the schema of the module that owns it. Cross-module references use UUIDs — never foreign keys across schemas.

## Agent Notes

- Primary key: UUID generated on the application side with `@UuidGenerator`
- All tables have `created_at` and `updated_at` of type `TIMESTAMPTZ`
- Never expose JPA entities outside the `internal/` package — always use DTOs

---

### Product

**Module: catalog** — schema `smx_catalog`

| Field | Type | Notes |
|---|---|---|
| `id` | UUID | PK |
| `name` | VARCHAR(500) | |
| `description` | TEXT | |
| `price` | NUMERIC(19,4) | |
| `category` | VARCHAR(255) | |
| `average_rating` | DECIMAL(3,2) | Updated by ReviewCreatedEvent |
| `review_count` | INTEGER | Incremental counter used to recalculate `average_rating` |
| `search_score` | DOUBLE PRECISION | Updated by analytics |
| `created_at` | TIMESTAMPTZ | |
| `updated_at` | TIMESTAMPTZ | |

Elasticsearch document (index `products`): mirrors the searchable product data used for ranking (`id`, `name`, `description`, `price`, `category`, `averageRating`, `searchScore`).

---

### Stock

**Module: warehouse** — schema `smx_warehouse`

| Field | Type | Notes |
|---|---|---|
| `id` | UUID | PK |
| `product_id` | UUID | Reference to Product (no cross-schema FK) |
| `quantity_total` | INTEGER | Total physical stock |
| `quantity_reserved` | INTEGER | Reserved by carts and ongoing orders |
| `version` | INTEGER | For optimistic locking (`@Version`) |
| `updated_at` | TIMESTAMPTZ | |

`quantity_available = quantity_total - quantity_reserved`

---

### Cart / CartItem

**Module: cart** — schema `smx_cart`

**carts**

| Field | Type | Notes |
|---|---|---|
| `id` | UUID | PK |
| `user_id` | VARCHAR(255) | User ID from JWT |
| `created_at` | TIMESTAMPTZ | |
| `updated_at` | TIMESTAMPTZ | |

**cart_items**

| Field | Type | Notes |
|---|---|---|
| `id` | UUID | PK |
| `cart_id` | UUID | FK → carts.id |
| `product_id` | UUID | Reference to Product |
| `quantity` | INTEGER | |
| `unit_price` | NUMERIC(19,4) | Snapshotted on addition |
| `added_at` | TIMESTAMPTZ | |

---

### Order / OrderItem

**Module: order** — schema `smx_order`

**orders**

| Field | Type | Notes |
|---|---|---|
| `id` | UUID | PK |
| `user_id` | VARCHAR(255) | User ID from Keycloak |
| `status` | VARCHAR(50) | PENDING, CONFIRMED, PAYMENT_PENDING, PAYMENT_FAILED, PREPARING, SHIPPED, DELIVERED, CANCELLED |
| `total_amount` | NUMERIC(19,4) | |
| `shipping_address` | TEXT | Formatted shipping address string |
| `created_at` | TIMESTAMPTZ | |
| `updated_at` | TIMESTAMPTZ | |

**order_items**

| Field | Type | Notes |
|---|---|---|
| `id` | UUID | PK |
| `order_id` | UUID | FK → orders.id |
| `product_id` | UUID | Reference to Product |
| `product_name` | VARCHAR(500) | Snapshotted |
| `unit_price` | NUMERIC(19,4) | Snapshotted |
| `quantity` | INTEGER | |

---

### Payment

**Module: payment** — schema `smx_payment`

| Field | Type | Notes |
|---|---|---|
| `id` | UUID | PK |
| `order_id` | UUID | UNIQUE — idempotency key |
| `amount` | NUMERIC(19,4) | |
| `status` | VARCHAR(50) | PENDING, SUCCESS, FAILED |
| `transaction_id` | VARCHAR(255) | Nullable |
| `failure_reason` | VARCHAR(255) | Nullable |
| `processed_at` | TIMESTAMPTZ | |

---

### Shipment

**Module: shipment** — schema `smx_shipment`

| Field | Type | Notes |
|---|---|---|
| `id` | UUID | PK |
| `order_id` | UUID | UNIQUE |
| `user_id` | VARCHAR(255) | User ID used for notification/tracking |
| `tracking_number` | VARCHAR(100) | |
| `carrier` | VARCHAR(100) | Simulated |
| `status` | VARCHAR(50) | PENDING, PICKED_UP, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED, FAILED |
| `shipped_at` | TIMESTAMPTZ | Nullable |
| `estimated_delivery` | TIMESTAMPTZ | Nullable |
| `created_at` | TIMESTAMPTZ | |

---

### Review

**Module: review** — schema `smx_review`

| Field | Type | Notes |
|---|---|---|
| `id` | UUID | PK |
| `product_id` | UUID | Reference to Product |
| `user_id` | VARCHAR(255) | |
| `order_id` | UUID | Reference to the order that justifies the review |
| `rating` | INTEGER | 1-5 |
| `text` | TEXT | |
| `created_at` | TIMESTAMPTZ | |
