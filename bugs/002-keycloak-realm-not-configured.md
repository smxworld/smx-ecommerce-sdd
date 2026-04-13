---
title: "Keycloak realm smxworld not imported on startup"
status: resolved
author: ""
created-at: "2026-04-13T00:00:00.000Z"
---

# Keycloak realm smxworld not imported on startup

## Error

The frontend shows "Page not found" on load because Keycloak
cannot find the `smxworld` realm. The realm export is not imported
automatically on container startup.

## Expected behavior

Anyone who clones the repository and runs `docker compose up -d` must
have Keycloak already configured with:
- Realm `smxworld`
- Client `smxworld-frontend` (public) and `smxworld-backend` (confidential)
- User `acquirente@smx.local` with password `password123` and role `ROLE_USER`
- User `operatore@smx.local` with password `password123` and role `ROLE_OPERATOR`

## Root cause

The `docker-compose.yml` does not mount the realm export and does not pass
the `--import-realm` flag to Keycloak.

## Fix

1. Create `infrastructure/keycloak/smxworld-realm.json` with the complete realm export
2. Update `docker-compose.yml` to mount the folder and pass `--import-realm`
3. Update `application.yml` with `issuer-uri: http://localhost:8180/realms/smxworld`
4. Update the Keycloak JS configuration in the frontend with realm `smxworld`

## Steps to reproduce

```bash
docker compose up -d
open http://localhost:5173
```