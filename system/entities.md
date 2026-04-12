---
title: "Data Entities"
status: synced
author: ""
last-modified: "2026-04-10T00:00:00.000Z"
version: "1.0"
---

# Data Entities

Entità principali del sistema, raggruppate per modulo di appartenenza. Ogni entità vive nello schema del modulo che la possiede. Le cross-reference tra moduli avvengono tramite UUID — mai foreign key tra schemi diversi.

## Agent Notes

- Primary key: UUID generato lato applicazione con `@UuidGenerator`
- Tutte le tabelle hanno `created_at` e `updated_at` di tipo `TIMESTAMPTZ`
- Mai esporre entità JPA fuori dal package `internal/` — usare sempre DTO

---

### Product

**Modulo: catalog** — schema `smx_catalog`

| Campo | Tipo | Note |
|---|---|---|
| `id` | UUID | PK |
| `name` | VARCHAR(255) | |
| `description` | TEXT | |
| `price` | DECIMAL(10,2) | |
| `category` | VARCHAR(100) | |
| `image_url` | VARCHAR(500) | |
| `average_rating` | DECIMAL(3,2) | Aggiornato da ReviewCreatedEvent |
| `created_at` | TIMESTAMPTZ | |
| `updated_at` | TIMESTAMPTZ | |

Documento Elasticsearch (indice `products`): tutti i campi sopra più `score` (float, per il ranking analytics).

---

### Stock

**Modulo: warehouse** — schema `smx_warehouse`

| Campo | Tipo | Note |
|---|---|---|
| `id` | UUID | PK |
| `product_id` | UUID | Riferimento a Product (no FK cross-schema) |
| `quantity_total` | INTEGER | Stock fisico totale |
| `quantity_reserved` | INTEGER | Prenotato da carrelli e ordini in corso |
| `version` | INTEGER | Per optimistic locking (`@Version`) |
| `updated_at` | TIMESTAMPTZ | |

`quantity_available = quantity_total - quantity_reserved`

---

### Cart / CartItem

**Modulo: cart** — schema `smx_cart`

**carts**

| Campo | Tipo | Note |
|---|---|---|
| `id` | UUID | PK |
| `user_id` | VARCHAR(255) | ID utente da JWT |
| `created_at` | TIMESTAMPTZ | |
| `updated_at` | TIMESTAMPTZ | |

**cart_items**

| Campo | Tipo | Note |
|---|---|---|
| `id` | UUID | PK |
| `cart_id` | UUID | FK → carts.id |
| `product_id` | UUID | Riferimento a Product |
| `quantity` | INTEGER | |
| `unit_price` | DECIMAL(10,2) | Snapshotted all'aggiunta |
| `added_at` | TIMESTAMPTZ | |

---

### Order / OrderItem

**Modulo: order** — schema `smx_order`

**orders**

| Campo | Tipo | Note |
|---|---|---|
| `id` | UUID | PK |
| `user_id` | VARCHAR(255) | ID utente da Keycloak |
| `status` | VARCHAR(50) | PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED |
| `total_amount` | DECIMAL(10,2) | |
| `shipping_address` | JSONB | |
| `created_at` | TIMESTAMPTZ | |
| `updated_at` | TIMESTAMPTZ | |

**order_items**

| Campo | Tipo | Note |
|---|---|---|
| `id` | UUID | PK |
| `order_id` | UUID | FK → orders.id |
| `product_id` | UUID | Riferimento a Product |
| `product_name` | VARCHAR(255) | Snapshotted |
| `unit_price` | DECIMAL(10,2) | Snapshotted |
| `quantity` | INTEGER | |

---

### Payment

**Modulo: payment** — schema `smx_payment`

| Campo | Tipo | Note |
|---|---|---|
| `id` | UUID | PK |
| `order_id` | UUID | UNIQUE — idempotency key |
| `amount` | DECIMAL(10,2) | |
| `status` | VARCHAR(50) | PENDING, SUCCESS, FAILED |
| `transaction_id` | VARCHAR(255) | Nullable |
| `failure_reason` | VARCHAR(255) | Nullable |
| `processed_at` | TIMESTAMPTZ | |

---

### Shipment

**Modulo: shipment** — schema `smx_shipment`

| Campo | Tipo | Note |
|---|---|---|
| `id` | UUID | PK |
| `order_id` | UUID | UNIQUE |
| `tracking_number` | VARCHAR(100) | |
| `carrier` | VARCHAR(100) | Simulato |
| `status` | VARCHAR(50) | PENDING, SHIPPED, DELIVERED |
| `shipped_at` | TIMESTAMPTZ | Nullable |
| `estimated_delivery` | DATE | Nullable |
| `created_at` | TIMESTAMPTZ | |

---

### Review

**Modulo: review** — schema `smx_review`

| Campo | Tipo | Note |
|---|---|---|
| `id` | UUID | PK |
| `product_id` | UUID | Riferimento a Product |
| `user_id` | VARCHAR(255) | |
| `order_id` | UUID | Riferimento all'ordine che giustifica la recensione |
| `rating` | INTEGER | 1-5 |
| `text` | TEXT | |
| `created_at` | TIMESTAMPTZ | |