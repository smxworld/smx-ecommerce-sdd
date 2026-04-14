---
title: "Elasticsearch index not populated"
status: resolved
author: ""
created-at: "2026-04-14T20:18:02.630Z"
---

# Elasticsearch index not populated

## Error

The catalog search relies on the Elasticsearch `products` index, but the
application never creates or populates that index from the catalog data stored
in PostgreSQL.

## Expected behavior

When the application starts, the `products` index exists and contains the
catalog products already loaded in the relational database, including seed
data used in development.

## Root cause

The catalog module defines `ProductDocument` and uses Elasticsearch for search,
but there is no startup synchronization that creates the index and reindexes
the current catalog products.

## Fix

Add a catalog startup initializer that:

```text
1. creates the Elasticsearch index and mapping if missing
2. reads all products from ProductJpaRepository
3. writes them to the products index
```
