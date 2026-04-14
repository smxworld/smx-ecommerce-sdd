---
title: "Email subjects are in Italian"
status: resolved
author: ""
created-at: "2026-04-14T00:00:00.000Z"
---

# Email subjects are in Italian

## Description

The email subject lines are still in Italian despite the email 
body templates being translated to English.

## Fix

Translate all email subjects in `NotificationService` to English:
- "Il tuo ordine è confermato" → "Your order has been confirmed"
- "Problema con il pagamento" → "There was a problem with your payment"
- "Il tuo ordine è in viaggio" → "Your order is on its way"