---
title: "Email subjects are in Italian"
status: resolved
author: ""
created-at: "2026-04-14T00:00:00.000Z"
---

# Email subjects are in Italian

## Description

After translating the email body templates to English (see bug 009), the
email subject lines in `NotificationService` remain in Italian. The
subjects "Il tuo ordine è confermato", "Problema con il pagamento", and
"Il tuo ordine è in viaggio" need to be translated to "Your order has
been confirmed", "There was a problem with your payment", and "Your order
is on its way".

## Steps to reproduce

1. Complete a purchase as `buyer@smxworld.local`
2. Open Mailpit at `http://localhost:8025`
3. The confirmation email arrives with an Italian subject line

## Expected behavior

All email subjects are in English, consistent with the translated body
templates.