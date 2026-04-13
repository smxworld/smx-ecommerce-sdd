---
title: "Missing event_publication table on startup"
status: resolved
author: ""
created-at: "2026-04-13T00:00:00.000Z"
---

# Missing event_publication table on startup

## Error

Schema-validation: missing table [event_publication]

## Description

The application fails to start because Spring Modulith requires an `event_publication` table for tracking domain events, but the Flyway migration that creates it is missing.

## Expected behavior

The application starts correctly and the `event_publication` table is created by Flyway before Hibernate validates the schema.

## Steps to reproduce

```bash
docker compose up -d
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```