---
title: "Catalog and Search"
status: synced
author: ""
last-modified: "2026-04-10T00:00:00.000Z"
version: "1.0"
---

# Catalog and Search

## User Stories

- As a buyer, I can search for products by name or description
- As a buyer, I can filter by category and price range
- As a buyer, I see products sorted by relevance
- As a buyer, I see stock availability
- As a buyer, I can view the product detail page

## Behavior

- Search uses Elasticsearch via the catalog module
- Each search publishes a `SearchPerformedEvent` consumed by analytics
- The analytics module publishes `SearchScoreUpdatedEvent`
- The catalog module consumes `SearchScoreUpdatedEvent` to update rankings
- The catalog module consumes `ReviewCreatedEvent` to update the average rating
- Stock availability is read from WarehouseApi on-demand

## Agent Notes

- CatalogApi exposes: `getProduct(UUID)`, `search(SearchQuery)`, `updateProductScore(UUID, double)`
- SearchQuery contains: `q`, `category`, `minPrice`, `maxPrice`, `page`, `size`
- The Elasticsearch document mirrors the Product fields plus `averageRating` and `score`
- Stock availability must NOT go into the Elasticsearch index
