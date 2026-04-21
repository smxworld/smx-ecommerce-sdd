---
title: "Catalog and Search"
status: synced
author: ""
last-modified: "2026-04-21T00:00:00.000Z"
version: "1.1"
---

# Catalog and Search

The catalog is the public-facing product index. It lets buyers discover
products through full-text search, apply filters, and view detailed product
information. Search relevance is continuously refined by an analytics
feedback loop.

## Behavior

### Searching for products

The buyer submits a search query, optionally combined with filters for
category, minimum price, and maximum price. The system forwards the query
to Elasticsearch through the catalog module and returns a paginated list of
matching [[Product]] records sorted by relevance score.

Each search publishes a `SearchPerformedEvent` consumed by the analytics
module. Analytics processes the event and publishes a
`SearchScoreUpdatedEvent` back to the catalog module, which updates the
relevance score on the corresponding Elasticsearch document. This creates a
feedback loop: products that perform well in searches rank higher over time.

If the query is empty and no filters are applied, the system returns all
products sorted by score descending. If no products match, the system
returns an empty page with zero results.

### Filtering

Filters are additive. The buyer can combine category (exact match), minimum
price (inclusive), and maximum price (inclusive). The system applies all
provided filters to the Elasticsearch query. Omitted filters are ignored —
they do not restrict results.

### Viewing product detail

The buyer requests a single [[Product]] by its UUID. The system returns the
full product record including name, description, category, price, average
rating, and current stock availability.

Stock availability is read from `WarehouseApi.getStock()` at request time
and is never cached in the Elasticsearch index. This ensures the buyer
always sees the real-time stock status.

If the product UUID does not exist, the system returns HTTP 404.

### Rating updates

When a buyer submits a review (see Reviews and Notifications), the review
module publishes a `ReviewCreatedEvent`. The catalog module consumes this
event and recalculates the average rating on the [[Product]] record and its
Elasticsearch document.

## Agent Notes

- CatalogApi exposes: `getProduct(UUID)`, `search(SearchQuery)`,
  `updateProductScore(UUID, double)`
- SearchQuery contains: `q`, `category`, `minPrice`, `maxPrice`, `page`,
  `size`
- The Elasticsearch document mirrors the [[Product]] fields plus
  `averageRating` and `score`
- Stock availability must NOT go into the Elasticsearch index — always
  fetch it live from WarehouseApi