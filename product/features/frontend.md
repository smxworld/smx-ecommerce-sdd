---
title: "Frontend React"
status: synced
author: ""
last-modified: "2026-04-12T00:00:00.000Z"
version: "1.0"
---

# Frontend React

React application implementing the complete purchase flow: product search → detail → cart → checkout → order confirmation.

## Frontend Stack

- **React 18** with Vite as build tool
- **React Router v6** for navigation
- **React Query (TanStack Query)** for API calls and caching
- **Tailwind CSS** for styling
- **Axios** for HTTP calls
- **Keycloak JS** for authentication

The frontend lives in `code/frontend/` and is a Vite project separate from the Spring Boot backend.

## Screens

### 1. Home / Search (`/`)

- Centered text search bar
- Filters: category (select) and price range (min/max)
- Product grid (3 columns on desktop, 1 on mobile)
- Each product card shows: placeholder image, name, price, average rating (stars), stock availability (green/red badge)
- Pagination at the bottom

### 2. Product Detail (`/products/:id`)

- Product image on the left, info on the right
- Name, category, price, average rating
- Stock availability badge
- Extended description
- Quantity input + "Add to cart" button
- Reviews section at the bottom (list with rating and text)

### 3. Cart (`/cart`)

- List of items in the cart
- Each row: product name, unit price, editable quantity input, subtotal, remove button
- Total at the bottom right
- "Proceed to checkout" button
- If cart is empty: message + link to home

### 4. Checkout (`/checkout`)

- Shipping address form: first name, last name, street, city, ZIP, country
- Order summary (readonly): item list + total
- "Confirm order" button
- Redirect to confirmation page after POST /api/checkout

### 5. Order Confirmation (`/orders/:orderId`)

- Confirmation message with success icon
- Order number
- Order status (badge)
- List of ordered items
- "Continue shopping" button → returns to home

## Authentication

- On app load, Keycloak JS checks whether the user is authenticated
- If not authenticated, redirect to Keycloak login page
- The JWT token is automatically attached to all API calls via an Axios interceptor
- The token is automatically refreshed before expiry

## API Calls

All calls go to `http://localhost:8080/api` (the Spring Boot backend).

| Screen | Method | Endpoint |
|---|---|---|
| Home/Search | GET | `/api/search?q=&category=&minPrice=&maxPrice=&page=&size=` |
| Detail | GET | `/api/products/:id` |
| Cart | GET | `/api/cart` |
| Add item | POST | `/api/cart/items` |
| Update quantity | PUT | `/api/cart/items/:productId` |
| Remove item | DELETE | `/api/cart/items/:productId` |
| Checkout | POST | `/api/checkout` |
| Order status | GET | `/api/orders/:orderId` |
| Reviews | GET | `/api/reviews/:productId` |

## Agent Notes

- The frontend project goes in `code/frontend/` — separate from `code/src/` of the backend
- Initialize with `npm create vite@latest frontend -- --template react`
- Configure the Vite proxy in `vite.config.js` to redirect `/api` to `http://localhost:8080`
- Folder structure:
  ```
  code/frontend/
    src/
      components/     ← reusable components (ProductCard, CartItem, etc.)
      pages/          ← screens (HomePage, ProductPage, CartPage, CheckoutPage, OrderPage)
      hooks/          ← custom React Query hooks
      api/            ← Axios functions for each endpoint
      auth/           ← Keycloak configuration
    public/
    index.html
    vite.config.js
    package.json
  ```
- Use placeholder images from `https://placehold.co/400x300` for products
- Rating stars: implement with Unicode characters ★/☆ without external libraries
- No UI component library (no MUI, no Ant Design) — Tailwind only
- CORS already configured in the Spring Boot backend for `http://localhost:5173`
- All user-facing frontend text must be in English, including placeholders,
  labels, buttons, loading/error states, empty states, badges, and navigation
  links across pages and reusable components
- Use English UI copy such as "Search products...", "All categories",
  "Min price", "Search", "Loading...", "No products found.",
  "Discover our products", "Add to cart", "Proceed to checkout",
  "Confirm order", and "Continue shopping"
