---
title: "Keycloak realm smxworld not imported on startup"
status: resolved
author: ""
created-at: "2026-04-13T00:00:00.000Z"
---

# Keycloak realm smxworld not imported on startup

## Description

The frontend shows "Page not found" on first load because Keycloak cannot
find the `smxworld` realm. The `docker-compose.yml` does not mount the
realm export file and does not pass the `--import-realm` flag to the
Keycloak container. As a result, anyone who clones the repository and runs
`docker compose up -d` gets a Keycloak instance with only the `master`
realm — no clients, no users, no roles.

The expected realm configuration includes: realm `smxworld`, client
`smxworld-frontend` (public), client `smxworld-backend` (confidential),
user `buyer@smxworld.local` with role `ROLE_USER`, and user
`operator@smxworld.local` with role `ROLE_OPERATOR`.

## Steps to reproduce

1. Clone the repository
2. Run `docker compose up -d`
3. Open `http://localhost:5173`
4. The frontend shows "Page not found" — Keycloak rejects the
   authentication request because the realm does not exist

## Expected behavior

The `smxworld` realm is automatically imported on container startup.
The frontend loads and redirects to the Keycloak login page where the
seed users can authenticate.