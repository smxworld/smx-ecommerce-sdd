---
title: "Review usernames and email templates are in Italian"
status: resolved
author: ""
created-at: "2026-04-14T00:00:00.000Z"
---

# Review usernames and email templates are in Italian

## Description

Two residual Italian-language issues found after the main translation
pass.

The first issue is that the seed reviews use Italian-style usernames
such as `utente4@smx.local` and `acquirente@smx.local`. These should
match the Keycloak realm users: `buyer@smxworld.local` and
`user4@smxworld.local`. The same mismatch exists in the Keycloak realm
export where `acquirente@smx.local` and `operatore@smx.local` should be
`buyer@smxworld.local` and `operator@smxworld.local`. The `README.md`
default users table must also be updated.

The second issue is that all Thymeleaf email templates in
`resources/templates/mail/` contain Italian text. The order confirmation,
payment failed, and order shipped templates need to be translated to
English.

## Steps to reproduce

1. Navigate to a product with seed reviews — reviewer names are in
   Italian
2. Complete a purchase — the confirmation email body is in Italian

## Expected behavior

Seed review usernames match the Keycloak realm users and use the
`@smxworld.local` domain. All email templates are in English.