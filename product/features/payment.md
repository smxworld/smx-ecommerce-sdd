---
title: "Pagamento"
status: synced
author: ""
last-modified: "2026-04-10T00:00:00.000Z"
version: "1.0"
---

# Pagamento

## User Stories

- Come acquirente, posso pagare con carta di credito
- Come acquirente, ricevo conferma se il pagamento va a buon fine
- Come acquirente, vengo notificato se il pagamento fallisce

## Comportamento

- Viene chiamato da order tramite `PaymentApi.processPayment()`
- Pubblica `PaymentSucceededEvent` o `PaymentFailedEvent`
- Idempotente: se `orderId` già processato restituisce il risultato precedente

## Pending Changes

- [ ] Supporto a metodi di pagamento multipli
- [ ] Gestione rimborsi

## Agent Notes

- Gateway simulato: accetta sempre pagamenti con importo < 10.000€
- PaymentApi espone solo: `processPayment(UUID orderId, BigDecimal amount)`
- Idempotenza garantita tramite `order_id` UNIQUE sulla tabella `payments`