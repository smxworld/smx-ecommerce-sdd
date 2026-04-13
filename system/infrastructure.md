---
title: "Infrastruttura"
status: synced
author: ""
last-modified: "2026-04-12T00:00:00.000Z"
version: "1.0"
---

# Infrastruttura

Configurazione completa dell'ambiente di sviluppo locale tramite Docker Compose.

## Componenti

| Componente | Immagine | Porta | Scopo |
|---|---|---|---|
| PostgreSQL | `postgres:17` | 5432 | Database principale (tutti gli schemi) |
| Keycloak | `quay.io/keycloak/keycloak:24` | 8180 | Autenticazione e autorizzazione |
| Elasticsearch | `elasticsearch:8.13.0` | 9200 | Search index prodotti |
| Mailhog | `mailhog/mailhog` | 1025 (SMTP), 8025 (UI) | Mock SMTP per email |

## Docker Compose

Il file `docker-compose.yml` sta nella root del progetto.

### PostgreSQL

- Un singolo container PostgreSQL
- Tutti gli schemi (`smx_catalog`, `smx_cart`, `smx_order`, ecc.) vengono creati automaticamente da Flyway all'avvio dell'applicazione
- Credenziali: `user=smx`, `password=smx`, `database=smx`

### Keycloak

- Realm: `smx`
- Client: `smx-frontend` (public client, per React) e `smx-backend` (confidential, per Spring Boot)
- Il realm viene importato automaticamente all'avvio da `infrastructure/keycloak/realm-export.json`
- URL admin: `http://localhost:8180/admin` — credenziali: `admin/admin`

### Elasticsearch

- Security disabilitata in sviluppo (`xpack.security.enabled=false`)
- Indice `products` creato automaticamente da Spring Data Elasticsearch all'avvio

### Mailhog

- Tutte le email inviate dall'applicazione finiscono qui
- UI web per vedere le email: `http://localhost:8025`

## Configurazione Spring Boot

Nel file `code/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/smx
    username: smx
    password: smx

  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8180/realms/smx

  elasticsearch:
    uris: http://localhost:9200

  mail:
    host: localhost
    port: 1025

keycloak:
  realm: smx
  auth-server-url: http://localhost:8180
```

## CORS

Il backend Spring Boot deve accettare richieste da `http://localhost:5173` (Vite dev server).

Configurare un `CorsConfigurationSource` bean che permette:
- Origins: `http://localhost:5173`
- Methods: GET, POST, PUT, DELETE, OPTIONS
- Headers: Authorization, Content-Type
- Allow credentials: true

## Avvio dell'ambiente

```bash
# 1. Avvia l'infrastruttura
docker compose up -d

# 2. Avvia il backend Spring Boot
cd code && mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 3. Avvia il frontend React
cd code/frontend && npm install && npm run dev
```

Backend disponibile su `http://localhost:8080`
Frontend disponibile su `http://localhost:5173`

## Agent Notes

- Creare `docker-compose.yml` nella root del progetto (non dentro `code/`)
- Creare `infrastructure/keycloak/realm-export.json` con il realm completo
- Il realm export deve includere: realm `smx`, client `smx-frontend` e `smx-backend`, i due utenti di seed, i ruoli `ROLE_USER` e `ROLE_OPERATOR`
- Elasticsearch in sviluppo non richiede autenticazione — impostare `xpack.security.enabled=false`
- Aggiungere un `healthcheck` su PostgreSQL nel docker-compose per evitare race condition all'avvio
- Il backend Spring Boot non gira dentro Docker in sviluppo — gira sulla macchina host e si connette ai container
