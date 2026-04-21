---
title: "Missing event_publication table on startup"
status: resolved
author: ""
created-at: "2026-04-13T00:00:00.000Z"
---

# Missing event_publication table on startup

## Description

The application fails to start with the error
`Schema-validation: missing table [event_publication]`. Spring Modulith
requires an `event_publication` table for tracking domain events, but no
Flyway migration creates it. Hibernate validates the schema on startup
and rejects the missing table before the application context loads.

## Steps to reproduce

1. Run `docker compose up -d` to start the infrastructure
2. Run `mvn spring-boot:run -Dspring-boot.run.profiles=dev`
3. The application fails with schema validation error

## Expected behavior

The application starts correctly. The `event_publication` table exists
in the database, created by a Flyway migration before Hibernate validates
the schema.