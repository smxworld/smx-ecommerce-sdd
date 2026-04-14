---
title: "Frontend search: wrong response field and no refetch on submit"
status: resolved
author: ""
created-at: "2026-04-14T00:00:00.000Z"
---

# Frontend search: wrong response field and no refetch on submit

## Bug 1: Products not displayed

The frontend reads `data.content` but the backend returns `data.items`.

File: `code/frontend/src/pages/HomePage.jsx`

```js
// wrong
const products = data?.content ?? []
// correct
const products = data?.items ?? []
```

## Bug 2: Cerca button does not trigger a new search

`handleSearch` calls `setPage(0)` but if page is already 0, 
React detects no state change and React Query does not refetch.

Fix: introduce a separate `searchParams` state that is only 
updated on form submit, so React Query detects the change and refetches.

## Steps to reproduce

1. Open http://localhost:5173
2. Products are not shown despite a successful API response
3. Type something in the search box and click Cerca — no new API call is made