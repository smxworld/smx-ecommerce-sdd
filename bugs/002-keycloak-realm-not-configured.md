---
title: "Keycloak realm smxworld not imported on startup"
status: resolved
author: ""
created-at: "2026-04-13T00:00:00.000Z"
---

# Keycloak realm smxworld not imported on startup

## Error

Il frontend mostra "Page not found" al caricamento perché Keycloak 
non trova il realm `smxworld`. Il realm export non viene importato 
automaticamente all'avvio del container.

## Expected behavior

Chiunque cloni il repository e esegua `docker compose up -d` deve 
avere Keycloak già configurato con:
- Realm `smxworld`
- Client `smxworld-frontend` (public) e `smxworld-backend` (confidential)
- Utente `acquirente@smx.local` con password `password123` e ruolo `ROLE_USER`
- Utente `operatore@smx.local` con password `password123` e ruolo `ROLE_OPERATOR`

## Root cause

Il `docker-compose.yml` non monta il realm export e non passa 
il flag `--import-realm` a Keycloak.

## Fix

1. Creare `infrastructure/keycloak/smxworld-realm.json` con il realm export completo
2. Aggiornare `docker-compose.yml` per montare la cartella e passare `--import-realm`
3. Aggiornare `application.yml` con `issuer-uri: http://localhost:8180/realms/smxworld`
4. Aggiornare la configurazione Keycloak JS nel frontend con il realm `smxworld`

## Steps to reproduce

```bash
docker compose up -d
open http://localhost:5173
```