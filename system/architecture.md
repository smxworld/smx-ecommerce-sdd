---
title: "Architecture"
status: synced
author: ""
last-modified: "2026-04-10T00:00:00.000Z"
version: "2.0"
---

# Architettura

SmxECommerce è un **modulith** costruito con Spring Modulith. L'applicazione gira in un singolo processo Spring Boot, ma è strutturata internamente in moduli fortemente delimitati che rispettano gli stessi bounded context di un'architettura a microservizi.

L'obiettivo architetturale esplicito è: **pronto a diventare microservizi con il minimo sforzo possibile**.

## Principi fondamentali

1. **Moduli con confini espliciti** — ogni modulo espone una API pubblica (interfacce Java nel package root del modulo). Nessun modulo accede mai ai package interni di un altro.
2. **Comunicazione tra moduli solo tramite API pubblica o eventi** — mai chiamate dirette a classi interne.
3. **Database separati per modulo** — ogni modulo ha il proprio schema PostgreSQL. Nessuna JOIN tra schemi diversi.
4. **Eventi di dominio in-process** — Spring Application Events per la comunicazione asincrona. Pronti per essere esternalizzati su Kafka senza modificare produttori e consumatori.

## Bounded Context e Moduli

| Modulo | Package | Responsabilità |
|---|---|---|
| `identity` | `com.smx.identity` | Integrazione Keycloak, JWT validation |
| `catalog` | `com.smx.catalog` | Prodotti, categorie, search |
| `cart` | `com.smx.cart` | Carrello utente |
| `order` | `com.smx.order` | Lifecycle ordine, state machine |
| `payment` | `com.smx.payment` | Processing pagamenti |
| `warehouse` | `com.smx.warehouse` | Stock, prenotazioni |
| `shipment` | `com.smx.shipment` | Spedizioni e tracking |
| `review` | `com.smx.review` | Recensioni prodotti |
| `notification` | `com.smx.notification` | Email all'utente |
| `analytics` | `com.smx.analytics` | Analytics di ricerca |

## Struttura di ogni modulo

```
com.smx.<module>/
  <Module>Api.java          ← interfaccia pubblica (l'unico punto di accesso dall'esterno)
  <Module>Events.java       ← eventi di dominio pubblicati dal modulo (classi record)
  internal/
    domain/                 ← entità, value objects
    application/            ← use cases, service
    infrastructure/         ← repository, adapter esterni
```

I package sotto `internal/` sono invisibili agli altri moduli. Spring Modulith lo verifica automaticamente con `@ApplicationModuleTest`.

## Strategia di migrazione a microservizi

Quando un modulo deve diventare un servizio autonomo, il percorso è:

1. **Database** — già separato per schema, basta estrarre la connessione
2. **Eventi** — aggiungere `@Externalized("topic-name")` sull'evento e la dipendenza `spring-modulith-events-kafka`. Produttori e consumatori non cambiano una riga.
3. **API sincrone** — le chiamate Java tra moduli diventano chiamate REST. Questo è il vero lavoro di migrazione: ogni `<Module>Api.java` diventa un client HTTP.

## Entry Point

L'applicazione espone un'unica API REST verso l'esterno tramite il modulo `api` (BFF interno):

```
com.smx.api/
  rest/                     ← controller REST esposti al frontend
```

I controller chiamano le API pubbliche dei moduli interni. Non accedono mai direttamente ai package `internal/`.

## Autenticazione

Keycloak gestisce l'autenticazione. Il modulo `identity` valida il JWT su ogni richiesta tramite Spring Security OAuth2 Resource Server. Gli altri moduli ricevono il `userId` come parametro — non dipendono da Keycloak direttamente.

## Agent Notes

- Il progetto è un singolo modulo Maven con Spring Boot 3.x e Spring Modulith
- Package root: `com.smx.ecommerce`
- Ogni modulo è un package Java, non un progetto Maven separato
- Usare `@ApplicationModuleTest` per verificare i confini di ogni modulo
- Spring Modulith genera automaticamente la documentazione dell'architettura con `ApplicationModules.of(SmxApplication.class).verify()`
- Il codice va in `code/` nella root del progetto
