---
title: "Fix Mailhog Docker image for Apple Silicon"
status: applied
author: ""
created-at: "2026-04-14T00:00:00.000Z"
---

# Fix Mailhog Docker image for Apple Silicon

## Description

Mailhog's official image `mailhog/mailhog` is only available for 
`linux/amd64` and generates a warning on Apple Silicon (linux/arm64/v8).

## Fix

Replace `mailhog/mailhog` with `axllent/mailpit` in `docker-compose.yml`, 
which is a modern Mailhog-compatible alternative with native ARM64 support.

Same ports: 1025 (SMTP), 8025 (UI).
No changes required in the Spring Boot mail configuration.