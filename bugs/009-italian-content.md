---
title: "Review usernames and email templates are in Italian"
status: resolved
author: ""
created-at: "2026-04-14T00:00:00.000Z"
---

# Review usernames and email templates are in Italian

## Bug 1: Review usernames are in Italian

The seed reviews use Italian-style usernames like `utente4@smx.local` 
and `acquirente@smx.local`. 

Update the seed migration `V2__seed_reviews.sql` to use English-style 
usernames consistent with the Keycloak realm users:
- `acquirente@smx.local` → `buyer@smxworld.local`
- `utente4@smx.local` → `user4@smxworld.local`

Also update the Keycloak realm export `smxworld-realm.json` to use 
the same usernames:
- `acquirente@smx.local` → `buyer@smxworld.local`
- `operatore@smx.local` → `operator@smxworld.local`

Update `README.md` default users table accordingly.

## Bug 2: Email templates are in Italian

All Thymeleaf email templates in `code/src/main/resources/templates/mail/` 
contain Italian text. Translate all templates to English:
- Order confirmation email
- Payment failed email
- Order shipped email