---
title: "Move catalog search queries into the repository layer"
status: applied
author: ""
created-at: "2026-10-08T12:42:00.000Z"
---

# Move catalog search queries into the repository layer

## Description

The package layout in `system/architecture.md` places database access and repositories in the `repository` package of each module, and places controllers, application classes and other configuration in `controller` and `service`. The document does not say where Elasticsearch queries are built and executed.

Today the `catalog` module has this split:

- JPA queries are declared in repositories (`ProductJpaRepository`). The only `@Query` declarations in the project are in `OrderRepository` and `SearchLogRepository`.
- The Elasticsearch search is built and executed inside `CatalogService`: the service creates a `CriteriaQuery` from the search parameters and runs it through `ElasticsearchOperations`. A `ProductElasticsearchRepository` exists in `catalog/internal/repository/` and is not the single entry point for this search.
- `CatalogIndexInitializer` uses `ElasticsearchOperations` to prepare the index. It is an initialization class and sits in `service`, which matches the rule that other configuration goes to `service`.

The result is that the catalog module reaches its data stores in two different places. This change request makes the rule explicit and aligns the code with it.

## Changes

- Update `system/architecture.md`, in the section that describes the package layout, to state the following rule:
  - Queries against any data store, relational or Elasticsearch, are built and executed in classes under the `repository` package.
  - Classes that implement business logic in `service` call repositories and map the results. They do not use `EntityManager`, `JdbcTemplate` or `ElasticsearchOperations` directly.
  - Classes in `service` that only configure or initialize a data store, such as the catalog index initializer, are exempt.
- Before moving any code, make sure a test covers the current catalog search behavior (filters, sorting, pagination). If none does, add one first and make it pass on the current code.
- Align the `catalog` module with the updated rule, without changing behavior:
  - The construction of the Elasticsearch query from the search parameters and its execution with `ElasticsearchOperations` move from `CatalogService` into the `repository` package, in `ProductElasticsearchRepository` or in a dedicated search repository class, following the conventions already used in the module.
  - `CatalogService` passes its own search criteria object (text, category, price range, page) to that repository and maps the hits to the result type it returns today. It no longer imports `ElasticsearchOperations`, `CriteriaQuery` or other Spring Data Elasticsearch query types.
- The catalog search keeps the same filters, sorting, pagination and results. The existing tests continue to pass.
