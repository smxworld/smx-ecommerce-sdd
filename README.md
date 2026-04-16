# SmxECommerce

A full-stack e-commerce platform built as a **Spring Modulith** application, designed as an experiment in agentic development using [Story Driven Development (SDD)](https://github.com/applica-software-guru/sdd).

The project covers the complete purchase flow — product search, cart, checkout, payment, warehouse, shipment, reviews, and notifications — implemented as a modular monolith ready to evolve into microservices with minimal effort.

---

## Purpose

SmxECommerce is an experiment, not a production system. The goal is to explore two things in parallel:

1. **Agentic coding** — using AI coding agents (Claude Code and GitHub Copilot) to implement a non-trivial system from documentation alone, without writing code manually
2. **Story Driven Development** — validating SDD as a methodology for managing context and intent across a complex, multi-domain project

The architecture is intentionally realistic: bounded contexts, domain events, schema isolation, and a clear migration path to microservices. This makes it meaningful as a learning reference beyond the experiment itself.

---

## How It Was Built

The entire codebase was generated through the SDD workflow using both [Claude Code](https://www.anthropic.com/claude-code) and GitHub Copilot as agentic coding tools:

1. **Write the Story** — all features, entities, interfaces, and architectural decisions were documented first in `product/` and `system/` as Markdown files
2. **Run `sdd sync`** — the SDD CLI reads the documentation, detects what is new or changed, and generates a structured prompt
3. **Agent implements** — Claude Code or GitHub Copilot receives the prompt and implements the code
4. **Verify** — `mvn test` runs after every implementation cycle; `ApplicationModulesTest` enforces module boundaries automatically
5. **Mark and commit** — `sdd mark-synced` closes the cycle, then git commit

No code was written manually. Every change to the system starts with a documentation update, not a code change.

---

## Architecture

SmxECommerce is a **modulith** — a single Spring Boot application structured into strongly bounded modules that mirror microservice boundaries. The architecture is explicitly designed to be extracted into microservices with minimal effort.

### Modules

| Module | Package | Responsibility |
|---|---|---|
| `catalog` | `com.smxworld.ecommerce.catalog` | Products, search, Elasticsearch indexing |
| `cart` | `com.smxworld.ecommerce.cart` | Shopping cart per user |
| `order` | `com.smxworld.ecommerce.order` | Order lifecycle orchestration |
| `payment` | `com.smxworld.ecommerce.payment` | Payment processing |
| `warehouse` | `com.smxworld.ecommerce.warehouse` | Stock management and reservations |
| `shipment` | `com.smxworld.ecommerce.shipment` | Shipment tracking |
| `review` | `com.smxworld.ecommerce.review` | Product reviews |
| `notification` | `com.smxworld.ecommerce.notification` | Email notifications |
| `analytics` | `com.smxworld.ecommerce.analytics` | Search analytics and ranking |
| `identity` | `com.smxworld.ecommerce.identity` | JWT identity integration |

### Communication

- **Synchronous** — modules communicate via public Java interfaces (`CatalogApi`, `OrderApi`, etc.). No module accesses another module's `internal/` package.
- **Asynchronous** — domain events via Spring Application Events (`OrderConfirmedEvent`, `PaymentSucceededEvent`, etc.). Ready to be externalized to Kafka with a single annotation.

### Database

Each module owns a dedicated PostgreSQL schema (`smx_catalog`, `smx_order`, etc.). No cross-schema JOINs — cross-module data is retrieved through public APIs.

---

## Architecture Decision Log

**Modulith over microservices**
A distributed system has significant operational overhead that would obscure the didactic value of the project. A modulith enforces the same boundaries as microservices — through Spring Modulith's `ApplicationModuleTest` — while running as a single process. The migration path is explicit and documented.

**Spring Application Events over Kafka**
In-process events are sufficient for a single JVM. Kafka would require a broker, schema registry, and consumer group management with no benefit at this scale. Spring Modulith's `@Externalized` annotation makes the future migration a one-line change per event type.

**Separate PostgreSQL schemas over separate databases**
Schema isolation provides the same logical boundary as separate databases in development, without the operational complexity of managing multiple database connections and credentials. In a real microservices migration, each schema becomes its own database.

**No JPA entities across module boundaries**
Every module exposes only DTOs through its public API. JPA entities live exclusively in `internal/domain/`. This ensures that the public contract of a module never leaks implementation details, and that extraction to a microservice does not require changing the API surface.

---

## Tech Stack

**Backend**
- Java 25, Spring Boot 3.x, Spring Modulith 1.x
- PostgreSQL 17 (one schema per module)
- Elasticsearch 8.x (product search)
- Spring Security + Keycloak (OAuth2/JWT)
- Flyway (database migrations)
- Spring State Machine dependency (present in the build; the current order flow is orchestrated in service code)
- Spring Mail + Thymeleaf (email templates)

**Frontend**
- React 18, Vite 5
- React Router v6, TanStack Query v5
- Tailwind CSS, Axios, Keycloak JS

**Infrastructure (local development)**
- Docker Compose: PostgreSQL, Keycloak, Elasticsearch, Mailpit

---

## Project Structure

```
smx-ecommerce/
  product/                    # SDD Story — product documentation
    vision.md
    users.md
    features/
      catalog-search.md
      cart.md
      checkout-order.md
      payment.md
      warehouse-shipment.md
      reviews-notification.md
      frontend.md
      seed-data.md
  system/                     # SDD Story — system documentation
    architecture.md
    tech-stack.md
    entities.md
    interfaces.md
    infrastructure.md
  infrastructure/
    keycloak/
      smxworld-realm.json
  code/
    src/                      # Spring Boot application
    frontend/                 # React application
    pom.xml
  docker-compose.yml
  README.md
```

---

## Getting Started

### Prerequisites

- Java 25
- Maven 3.9+
- Node.js 20+
- Docker and Docker Compose

### Start the infrastructure

```bash
docker compose up -d
```

This starts Keycloak 26.0.8 and imports the `smxworld` realm automatically from
`infrastructure/keycloak/smxworld-realm.json`.

### Start the backend

```bash
cd code
mvn spring-boot:run
```

Backend available at `http://localhost:8080`

### Start the frontend

```bash
cd code/frontend
npm install
npm run dev
```

Frontend available at `http://localhost:5173`

### Default users

| Username | Password | Role |
|---|---|---|
| `buyer@smxworld.local` | `password123` | User |
| `operator@smxworld.local` | `password123` | Operator |

### Useful URLs

| Service | URL |
|---|---|
| Frontend | http://localhost:5173 |
| Backend API | http://localhost:8080 |
| Keycloak Admin | http://localhost:8180/admin (admin/admin) |
| Keycloak Realm | http://localhost:8180/realms/smxworld |
| Mailpit | http://localhost:8025 |
| Elasticsearch | http://localhost:9200 |

---

## SDD Workflow

This project uses [Story Driven Development](https://github.com/applica-software-guru/sdd). To contribute or extend the system:

1. Install the SDD CLI: `npm install -g @applica-software-guru/sdd`
2. Update or create documentation in `product/` or `system/` with `status: new` or `status: changed`
3. Run `sdd sync` to generate the implementation prompt
4. Pass the prompt to Claude Code or GitHub Copilot: `Run sdd sync and implement what it says.`
5. Verify: `mvn test` must pass, including `ApplicationModulesTest`
6. Close the cycle: `sdd mark-synced <file>` then `git commit`

Never modify the code directly for a feature that should start from a documentation change. The Story is the source of truth.

---

## Running Tests

```bash
# All tests including module boundary verification
cd code && mvn test

# Single module test
cd code && mvn test -Dtest=CatalogModuleTest
```

---

## License

MIT License — see [LICENSE](LICENSE) for details.
