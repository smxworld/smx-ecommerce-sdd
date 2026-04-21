---
title: "Frontend search does not display products and does not refetch on submit"
status: resolved
author: ""
created-at: "2026-04-14T00:00:00.000Z"
---

# Frontend search does not display products and does not refetch on submit

## Description

Two issues in the search page prevent products from being displayed.

The first issue is a field name mismatch. The frontend reads
`data.content` from the search API response, but the backend returns
results in `data.items`. No products are rendered even though the API
call succeeds.

The second issue is that the search button does not trigger a new API
call. The `handleSearch` function calls `setPage(0)`, but if the page
is already 0, React detects no state change and React Query does not
refetch. Typing a new query and clicking the button has no effect.

## Steps to reproduce

1. Open `http://localhost:5173`
2. The product grid is empty despite a successful API response visible
   in the browser network tab
3. Type a search term and click the search button
4. No new API call is made

## Expected behavior

Products appear in the grid on page load. Submitting a new search query
triggers a new API call and updates the results.