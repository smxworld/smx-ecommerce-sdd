---
title: "Keycloak realm import volume path incorrect for Keycloak 26"
status: resolved
author: ""
created-at: "2026-04-13T00:00:00.000Z"
---

# Keycloak realm import volume path incorrect for Keycloak 26

## Error

The `smxworld` realm is not imported on startup because the Docker volume
points to `/opt/keycloak/data/import` which does not exist in Keycloak 26.

ls: cannot access '/opt/keycloak/data/import/': No such file or directory

## Expected behavior

The `smxworld` realm is automatically imported on startup and appears
in the admin console alongside the `master` realm.

## Root cause

In Keycloak 26 the correct path for realm import is
`/opt/keycloak/conf/import`, not `/opt/keycloak/data/import`.

## Fix

In `docker-compose.yml` update the volume mount:

```yaml
volumes:
  - ./infrastructure/keycloak:/opt/keycloak/conf/import
```

## Steps to reproduce

```bash
docker compose up -d
docker exec smx-keycloak ls /opt/keycloak/data/import/
# ls: cannot access '/opt/keycloak/data/import/': No such file or directory
```
