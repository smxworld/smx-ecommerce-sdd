---
title: "Tech Stack"
status: synced
author: ""
last-modified: "2026-04-10T00:00:00.000Z"
version: "2.0"
---

# Tech Stack

## Runtime

| Componente | Tecnologia | Versione |
|---|---|---|
| Linguaggio | Java | 25 (LTS) |
| Framework | Spring Boot | 3.x |
| Modulith | Spring Modulith | 1.x |
| Build | Maven | 3.9+ |

## Persistence

Un singolo cluster PostgreSQL con schemi separati per modulo.

| Modulo | Schema PostgreSQL | Motivazione |
|---|---|---|
| `cart` | `smx_cart` | Struttura semplice, accesso frequente |
| `order` | `smx_order` | Transazionalità, lifecycle complesso |
| `payment` | `smx_payment` | Audit trail finanziario, idempotenza |
| `warehouse` | `smx_warehouse` | Concorrenza su stock, optimistic locking |
| `shipment` | `smx_shipment` | Tracking persistente |
| `review` | `smx_review` | Dati relazionali |
| `analytics` | `smx_analytics` | Aggregati |
| `catalog` | `smx_catalog` | Prodotti e categorie |

Ogni modulo configura il proprio `DataSource` puntando al proprio schema. Nessuna JOIN tra schemi diversi — i dati cross-modulo si ottengono tramite API pubblica.

## Messaggistica

**Spring Application Events** per la comunicazione asincrona in-process tra moduli.

```java
// Produttore (dentro un modulo)
applicationEventPublisher.publishEvent(new OrderConfirmedEvent(orderId, userId));

// Consumatore (in un altro modulo)
@ApplicationModuleListener
void on(OrderConfirmedEvent event) { ... }
```

### Percorso di migrazione a Kafka

Quando si vuole esternalizzare un evento su Kafka, il cambio è minimo:

1. Aggiungere dipendenza `spring-modulith-events-kafka`
2. Annotare l'evento con `@Externalized("smx.order-confirmed")`
3. Configurare il broker in `application.yml`

Produttori e consumatori non cambiano.

## Dipendenze Spring Boot

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

## Infrastruttura (Docker Compose — sviluppo)

Il Docker Compose in sviluppo è molto più snello rispetto a un'architettura a microservizi — un solo processo applicativo.

| Componente | Immagine | Porta |
|---|---|---|
| Applicazione SmxECommerce | build locale | 8080 |
| Keycloak | `quay.io/keycloak/keycloak:latest` | 8180 |
| PostgreSQL | `postgres:17` | 5432 |
| Elasticsearch | `elasticsearch:8.x` | 9200 |
| Mailhog (SMTP mock) | `mailhog/mailhog` | 1025 / 8025 |

Niente Kafka, niente Zookeeper, niente service discovery, niente Redis — tutto eliminato rispetto all'architettura a microservizi.

## Struttura del progetto

```
smx-ecommerce/
  product/                  ← Story SDD (product docs)
  system/                   ← Story SDD (system docs)
  code/                     ← codice sorgente
    src/
      main/
        java/com/smx/ecommerce/
          SmxECommerceApplication.java
          api/              ← controller REST (BFF interno)
          identity/         ← modulo identity
          catalog/          ← modulo catalog
          cart/             ← modulo cart
          order/            ← modulo order
          payment/          ← modulo payment
          warehouse/        ← modulo warehouse
          shipment/         ← modulo shipment
          review/           ← modulo review
          notification/     ← modulo notification
          analytics/        ← modulo analytics
        resources/
          application.yml
          db/migration/     ← script Flyway (suddivisi per schema)
      test/
    pom.xml
  docker-compose.yml
```

## Agent Notes

- Un solo `pom.xml` nella cartella `code/`
- Java 25, Spring Boot 3.x, Spring Modulith 1.x
- Non usare `spring.jpa.hibernate.ddl-auto=create` — solo Flyway
- Gli script Flyway vanno in `resources/db/migration/<schema>/` (es. `db/migration/smx_order/V1__init.sql`)
- Ogni modulo ha il proprio `application.yml` fragment caricato via `@ConfigurationProperties`
- Per i test di architettura: `ApplicationModules.of(SmxECommerceApplication.class).verify()` va eseguito come test base
- Elasticsearch per la ricerca prodotti — usare Spring Data Elasticsearch
