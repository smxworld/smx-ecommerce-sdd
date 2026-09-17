---
title: "Architecture"
status: synced
author: ""
last-modified: "2026-09-17T00:00:00.000Z"
version: "2.1"
---

# Architecture

SmxECommerce is a **modulith** built with Spring Modulith. The application runs as a single Spring Boot process, but is internally structured into strongly bounded modules that respect the same bounded contexts as a microservices architecture.

The explicit architectural goal is: **ready to become microservices with minimal effort**.

## Core principles

1. **Modules with explicit boundaries** — each module exposes a public API (Java interfaces in the module's root package). No module ever accesses the internal packages of another.
2. **Inter-module communication only via public API or events** — never direct calls to internal classes.
3. **Separate database per module** — each module has its own PostgreSQL schema. No JOINs across schemas.
4. **In-process domain events** — Spring Application Events for asynchronous communication. Ready to be externalized to Kafka without changing producers and consumers.

## Bounded Contexts and Modules

| Module | Package | Responsibility |
|---|---|---|
| `identity` | `com.smxworld.ecommerce.identity` | JWT validation and identity integration |
| `catalog` | `com.smxworld.ecommerce.catalog` | Products, categories, search |
| `cart` | `com.smxworld.ecommerce.cart` | User cart |
| `order` | `com.smxworld.ecommerce.order` | Order lifecycle orchestration |
| `payment` | `com.smxworld.ecommerce.payment` | Payment processing |
| `warehouse` | `com.smxworld.ecommerce.warehouse` | Stock and reservations |
| `shipment` | `com.smxworld.ecommerce.shipment` | Shipments and tracking |
| `review` | `com.smxworld.ecommerce.review` | Product reviews |
| `notification` | `com.smxworld.ecommerce.notification` | Email to users |
| `analytics` | `com.smxworld.ecommerce.analytics` | Search analytics |

## Structure of each module

```
com.smxworld.ecommerce.<module>/
  <Module>Api.java          ← public interface (the only entry point from outside)
  <Module>Events.java       ← domain events published by the module (record classes)
  internal/
    controller/             ← module-local REST controllers, when present
    service/                ← application services, orchestration, external adapters
    repository/             ← database access, repositories, Flyway configuration
    model/                  ← entities, records, value objects, search documents
```

The previous `application` / `domain` / `infrastructure` layout was abandoned because it made navigation harder without consistently delivering the intended separation. The simpler layout groups classes by their concrete role.

Move application classes to `service`, domain classes to `model`, and infrastructure classes according to their role: controllers to `controller`, repositories and database configuration to `repository`, and filters, clients and other configuration to `service`. Elasticsearch document models belong in `model`. Create packages only when there are classes to place in them.

This reorganisation changes only file locations, package declarations and imports. Method bodies, dependencies and behaviour remain unchanged; extracting queries from services is outside its scope. Preserve existing public APIs, DTOs and events at module roots, all `package-info.java` annotations, and the existing `api/rest` BFF entry point.

Verification requires a clean compilation, the existing Maven test suite and `ApplicationModules.of(SmxECommerceApplication.class).verify()`. Tests retain all existing methods and assertions; only package declarations, imports and file locations may follow relocated production classes. No legacy internal packages may remain, and a source comparison must confirm that no method body changed.

Packages under `internal/` are invisible to other modules. Spring Modulith verifies this automatically with `@ApplicationModuleTest`.

## Migration strategy to microservices

When a module needs to become a standalone service, the path is:

1. **Database** — already separated by schema, just extract the connection
2. **Events** — add `@Externalized("topic-name")` on the event and the `spring-modulith-events-kafka` dependency. Producers and consumers don't change a line.
3. **Synchronous APIs** — Java calls between modules become REST calls. This is the real migration work: each `<Module>Api.java` becomes an HTTP client.

## Entry Point

The application exposes a single REST API to the outside through the `api` module (internal BFF):

```
com.smxworld.ecommerce.api/
  rest/                     ← REST controllers exposed to the frontend
```

Controllers call the public APIs of internal modules. They never access `internal/` packages directly.

## Authentication

Keycloak manages authentication. The `identity` module validates the JWT on every request via Spring Security OAuth2 Resource Server. Other modules receive the `userId` as a parameter — they do not depend on Keycloak directly.

## Agent Notes

- The project is a single Maven module with Spring Boot 3.x and Spring Modulith
- Root package: `com.smxworld.ecommerce`
- Each module is a Java package, not a separate Maven project
- Use `ApplicationModules.of(SmxECommerceApplication.class).verify()` for the cross-module boundary check and `@ApplicationModuleTest` for focused module tests
- Spring Modulith documentation is generated from `ApplicationModules.of(SmxECommerceApplication.class)` in `ApplicationModulesTest`
- Code goes in `code/` at the project root
