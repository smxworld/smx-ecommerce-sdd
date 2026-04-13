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

L'applicazione non si avvia perché Spring Modulith richiede una tabella `event_publication` per il tracking degli eventi di dominio, ma la migration Flyway che la crea non è presente.

## Expected behavior

L'applicazione si avvia correttamente e la tabella `event_publication` viene creata da Flyway prima che Hibernate validi lo schema.

## Steps to reproduce

```bash
docker compose up -d
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```