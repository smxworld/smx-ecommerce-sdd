---
title: "Keycloak realm import volume path incorrect for Keycloak 26"
status: resolved
author: ""
created-at: "2026-04-13T00:00:00.000Z"
---

# Keycloak realm import volume path incorrect for Keycloak 26

## Description

After fixing the missing realm import (see bug 002), the `smxworld` realm
is still not imported because the Docker volume mount points to
`/opt/keycloak/data/import`, which does not exist in Keycloak 26. The
correct import path in Keycloak 26 is `/opt/keycloak/conf/import`.

## Steps to reproduce

1. Run `docker compose up -d`
2. Run `docker exec smx-keycloak ls /opt/keycloak/data/import/`
3. Output: `ls: cannot access '/opt/keycloak/data/import/': No such file
   or directory`
4. The `smxworld` realm does not appear in the Keycloak admin console

## Expected behavior

The volume mount points to `/opt/keycloak/conf/import`. The `smxworld`
realm is imported on startup and appears in the admin console alongside
the `master` realm.