---
title: "Users"
status: synced
author: ""
last-modified: "2026-04-10T00:00:00.000Z"
version: "1.0"
---

# Utenti

## Acquirente

L'utente principale della piattaforma. Si registra, naviga il catalogo, acquista prodotti e traccia i propri ordini.

### Bisogni

- Trovare prodotti rapidamente tramite ricerca e filtri
- Aggiungere prodotti al carrello e modificarlo liberamente
- Completare l'acquisto in pochi step con pagamento sicuro
- Ricevere conferme e aggiornamenti sullo stato dell'ordine
- Lasciare recensioni sui prodotti acquistati

### Comportamento atteso

- Accede tramite login OAuth2 (Keycloak)
- Il carrello persiste tra sessioni diverse
- Può avere più ordini attivi contemporaneamente
- Riceve notifiche via email per ogni cambio di stato dell'ordine

## Operatore di Back-Office

Utente interno con privilegi elevati. Gestisce il magazzino, monitora gli ordini e aggiorna il catalogo.

### Bisogni

- Vedere tutti gli ordini e il loro stato corrente
- Aggiornare la disponibilità dei prodotti in magazzino
- Gestire il catalogo prodotti
- Avviare manualmente il processo di spedizione

### Comportamento atteso

- Accede con ruolo `ROLE_OPERATOR`
- Ha accesso diretto a endpoint amministrativi dedicati via REST
- Non interagisce con il frontend pubblico