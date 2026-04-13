---
title: "Tech Stack"
status: synced
author: ""
last-modified: "2026-04-10T00:00:00.000Z"
version: "2.0"
---

# Tech Stack

## Runtime

| Component | Technology | Version |
|---|---|---|
| Language | Java | 25 (LTS) |
| Framework | Spring Boot | 3.x |
| Modulith | Spring Modulith | 1.x |
| Build | Maven | 3.9+ |

## Frontend

| Component | Technology | Version |
|---|---|---|
| Framework | React | 18 |
| Build tool | Vite | 5.x |
| Routing | React Router | v6 |
| Data fetching | TanStack Query (React Query) | v5 |
| Styling | Tailwind CSS | v3 |
| HTTP client | Axios | 1.x |
| Authentication | Keycloak JS | 24.x |

The frontend lives in `code/frontend/` and is a Vite project separate from the backend. In development it runs on `http://localhost:5173` and points to the backend on `http://localhost:8080` via Vite proxy.

## Persistence

A single PostgreSQL cluster with separate schemas per module.

| Module | PostgreSQL Schema | Rationale |
|---|---|---|
| `cart` | `smx_cart` | Simple structure, frequent access |
| `order` | `smx_order` | Transactionality, complex lifecycle |
| `payment` | `smx_payment` | Financial audit trail, idempotency |
| `warehouse` | `smx_warehouse` | Stock concurrency, optimistic locking |
| `shipment` | `smx_shipment` | Persistent tracking |
| `review` | `smx_review` | Relational data |
| `analytics` | `smx_analytics` | Aggregates |
| `catalog` | `smx_catalog` | Products and categories |

Each module configures its own `DataSource` pointing to its own schema. No JOINs across schemas — cross-module data is obtained via the public API.

## Messaging

**Spring Application Events** for asynchronous in-process communication between modules.

```java
// Producer (inside a module)
applicationEventPublisher.publishEvent(new OrderConfirmedEvent(orderId, userId));

// Consumer (in another module)
@ApplicationModuleListener
void on(OrderConfirmedEvent event) { ... }
```

### Migration path to Kafka

When an event needs to be externalized to Kafka, the change is minimal:

1. Add dependency `spring-modulith-events-kafka`
2. Annotate the event with `@Externalized("smx.order-confirmed")`
3. Configure the broker in `application.yml`

Producers and consumers do not change.

## Spring Boot Dependencies

```xml
<!-- Core -->
<dependency>spring-boot-starter-web</dependency>
<dependency>spring-boot-starter-data-jpa</dependency>
<dependency>spring-boot-starter-security</dependency>
<dependency>spring-boot-starter-oauth2-resource-server</dependency>

<!-- Modulith -->
<dependency>spring-modulith-starter-core</dependency>
<dependency>spring-modulith-starter-jpa</dependency>
<dependency>spring-modulith-events-api</dependency>

<!-- Database -->
<dependency>spring-boot-starter-flyway</dependency>
<dependency>postgresql (driver)</dependency>

<!-- Search -->
<dependency>spring-boot-starter-data-elasticsearch</dependency>

<!-- Mail -->
<dependency>spring-boot-starter-mail</dependency>
<dependency>spring-boot-starter-thymeleaf</dependency>

<!-- State Machine (Order module) -->
<dependency>spring-statemachine-core</dependency>

<!-- Testing -->
<dependency>spring-modulith-starter-test</dependency>
```

## Infrastructure (Docker Compose — development)

The Docker Compose in development is much leaner than a microservices architecture — a single application process.

| Component | Image | Port |
|---|---|---|
| SmxECommerce Application | local build | 8080 |
| Keycloak | `quay.io/keycloak/keycloak:latest` | 8180 |
| PostgreSQL | `postgres:17` | 5432 |
| Elasticsearch | `elasticsearch:8.x` | 9200 |
| Mailhog (SMTP mock) | `mailhog/mailhog` | 1025 / 8025 |

No Kafka, no Zookeeper, no service discovery, no Redis — all eliminated compared to the microservices architecture.

## Project Structure

```
smx-ecommerce/
  product/                  ← SDD stories (product docs)
  system/                   ← SDD stories (system docs)
  code/                     ← source code
    src/
      main/
        java/com/smx/ecommerce/
          SmxECommerceApplication.java
          api/              ← REST controllers (internal BFF)
          identity/         ← identity module
          catalog/          ← catalog module
          cart/             ← cart module
          order/            ← order module
          payment/          ← payment module
          warehouse/        ← warehouse module
          shipment/         ← shipment module
          review/           ← review module
          notification/     ← notification module
          analytics/        ← analytics module
        resources/
          application.yml
          db/migration/     ← Flyway scripts (split by schema)
      test/
    pom.xml
  docker-compose.yml
```

## Agent Notes

- A single `pom.xml` in the `code/` folder
- Java 25, Spring Boot 3.x, Spring Modulith 1.x
- Do not use `spring.jpa.hibernate.ddl-auto=create` — Flyway only
- Flyway scripts go in `resources/db/migration/<schema>/` (e.g., `db/migration/smx_order/V1__init.sql`)
- Each module has its own `application.yml` fragment loaded via `@ConfigurationProperties`
- For architecture tests: `ApplicationModules.of(SmxECommerceApplication.class).verify()` should be run as the base test
- Elasticsearch for product search — use Spring Data Elasticsearch
