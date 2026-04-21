---
title: "Elasticsearch index not populated on startup"
status: resolved
author: ""
created-at: "2026-04-14T00:00:00.000Z"
---

# Elasticsearch index not populated on startup

## Description

The catalog search relies on the Elasticsearch `products` index, but the
application never creates or populates it. The catalog module defines a
`ProductDocument` and uses Elasticsearch for search queries, yet there is
no startup synchronization that creates the index, applies the mapping, and
reindexes the products already present in the relational database.

As a result, the search returns zero results even though products exist in
the PostgreSQL `smx_catalog` schema.

## Steps to reproduce

1. Start the infrastructure with `docker compose up -d`
2. Start the application
3. Open the frontend and perform a search
4. No products are returned despite seed data being present in PostgreSQL

## Expected behavior

On startup, the catalog module creates the Elasticsearch `products` index
if it does not exist, reads all products from the relational database, and
writes them to the index. Search returns results immediately after
application startup.