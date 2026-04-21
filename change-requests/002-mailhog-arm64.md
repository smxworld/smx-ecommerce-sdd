---
title: "Replace Mailhog with Mailpit for ARM64 compatibility"
status: applied
author: ""
created-at: "2026-04-14T00:00:00.000Z"
---

# Replace Mailhog with Mailpit for ARM64 compatibility

## Description

The official Mailhog Docker image (`mailhog/mailhog`) is built only for
`linux/amd64`. On Apple Silicon machines (ARM64), Docker runs it under
emulation, which produces platform warnings and can cause instability
during development. Since the project targets a local development setup
that should work on both Intel and ARM hardware, the SMTP mock service
needs a multi-architecture image.

Mailpit (`axllent/mailpit`) is a modern, actively maintained drop-in
replacement for Mailhog with native ARM64 support. It uses the same
default ports — 1025 for SMTP and 8025 for the web UI — and requires no
changes to the Spring Boot mail configuration.

## Changes

- Update `infrastructure/docker-compose.yml` to replace the
  `mailhog/mailhog` image with `axllent/mailpit`