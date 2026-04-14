---
title: "Infrastructure"
status: synced
author: ""
last-modified: "2026-04-12T00:00:00.000Z"
version: "1.0"
---

# Infrastructure

Complete configuration of the local development environment via Docker Compose.

## Components

| Component | Image | Port | Purpose |
|---|---|---|---|
| PostgreSQL | `postgres:17` | 5432 | Main database (all schemas) |
| Keycloak | `quay.io/keycloak/keycloak:26.0.8` | 8180 | Authentication and authorization |
| Elasticsearch | `elasticsearch:8.13.0` | 9200 | Product search index |
| Mailpit | `axllent/mailpit` | 1025 (SMTP), 8025 (UI) | Mock SMTP for emails |

## Docker Compose

The `docker-compose.yml` file is in the project root.

### PostgreSQL

- A single PostgreSQL container
- All schemas (`smx_catalog`, `smx_cart`, `smx_order`, etc.) are created automatically by Flyway on application startup
- Credentials: `user=smx`, `password=smx`, `database=smx`

### Keycloak

- Realm: `smxworld`
- Client: `smxworld-frontend` (public client, for React) and `smxworld-backend` (bearer-only, for Spring Boot)
- On startup Keycloak imports `infrastructure/keycloak/smxworld-realm.json` via the `/opt/keycloak/data/import` mount and `start-dev --import-realm`
- Admin URL: `http://localhost:8180/admin` — credentials: `admin/admin`

### Elasticsearch

- Security disabled in development (`xpack.security.enabled=false`)
- `products` index created automatically by Spring Data Elasticsearch on startup

### Mailpit

- All emails sent by the application end up here
- Web UI to view emails: `http://localhost:8025`

## Spring Boot Configuration

In the file `code/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/smx_ecommerce
    username: smx
    password: smx

  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8180/realms/smxworld

  elasticsearch:
    uris: http://localhost:9200

  mail:
    host: localhost
    port: 1025

keycloak:
  realm: smxworld
  auth-server-url: http://localhost:8180
```

## CORS

The Spring Boot backend must accept requests from `http://localhost:5173` (Vite dev server).

Configure a `CorsConfigurationSource` bean that allows:
- Origins: `http://localhost:5173`
- Methods: GET, POST, PUT, DELETE, OPTIONS
- Headers: Authorization, Content-Type
- Allow credentials: true

## Starting the environment

```bash
# 1. Start the infrastructure
docker compose up -d

# 2. Start the Spring Boot backend
cd code && mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 3. Start the React frontend
cd code/frontend && npm install && npm run dev
```

Backend available at `http://localhost:8080`
Frontend available at `http://localhost:5173`

## Agent Notes

- Create `docker-compose.yml` in the project root (not inside `code/`)
- Create `infrastructure/keycloak/smxworld-realm.json` with the complete realm
- The realm export must include: realm `smxworld`, clients `smxworld-frontend` and `smxworld-backend`, the two seed users, roles `ROLE_USER` and `ROLE_OPERATOR`
- Mount `infrastructure/keycloak/` to `/opt/keycloak/data/import` and start Keycloak with `start-dev --import-realm`
- Elasticsearch in development does not require authentication — set `xpack.security.enabled=false`
- Add a `healthcheck` on PostgreSQL in docker-compose to avoid race conditions on startup
- The Spring Boot backend does not run inside Docker in development — it runs on the host machine and connects to the containers
