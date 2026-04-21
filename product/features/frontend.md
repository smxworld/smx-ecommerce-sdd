---
title: "Frontend React"
status: synced
author: ""
last-modified: "2026-04-21T00:00:00.000Z"
version: "1.1"
---

# Frontend React

The frontend is a single-page React application that implements the complete
purchase flow: product search, product detail, cart management, checkout,
and order confirmation. It runs as a standalone Vite project separate from
the Spring Boot backend and communicates exclusively through REST APIs.

## Behavior

### Authentication

On application load, Keycloak JS checks whether the buyer is authenticated.
If the buyer is not authenticated, the system redirects to the Keycloak
login page. After successful login, the JWT token is stored in memory.

An Axios interceptor attaches the token to every API request as a Bearer
header. The token is refreshed automatically before expiry — the buyer is
never forced to re-authenticate during an active session.

### Navigation

The application uses React Router v6 with the following routes:

- `/` — Home and search
- `/products/:id` — Product detail
- `/cart` — Cart
- `/checkout` — Checkout
- `/orders/:orderId` — Order confirmation

All navigation is client-side. The backend serves no HTML pages.

### API communication

All API calls target `http://localhost:8080/api` through a Vite dev proxy
that redirects `/api` to the backend. React Query (TanStack Query) manages
server state, caching, and automatic refetching.

| Screen | Method | Endpoint |
|---|---|---|
| Home / Search | GET | `/api/search?q=&category=&minPrice=&maxPrice=&page=&size=` |
| Product detail | GET | `/api/products/:id` |
| Cart | GET | `/api/cart` |
| Add item | POST | `/api/cart/items` |
| Update quantity | PUT | `/api/cart/items/:productId` |
| Remove item | DELETE | `/api/cart/items/:productId` |
| Checkout | POST | `/api/checkout` |
| Order status | GET | `/api/orders/:orderId` |
| Reviews | GET | `/api/reviews/:productId` |

## UX

### Home / Search (`/`)

The page opens with a centered text search bar. Below the search bar, the
buyer can filter by category (select dropdown) and price range (min/max
numeric inputs). Results appear in a product grid — three columns on
desktop, one column on mobile.

Each product card shows a placeholder image, name, price, average rating
displayed as stars, and a stock availability badge (green when available,
red when out of stock). Pagination controls appear at the bottom of the
grid.

If no products match, the system displays a "No products found" message.

### Product Detail (`/products/:id`)

The page displays the product image on the left and product information on
the right: name, category, price, average rating, stock availability badge,
and extended description. Below the description, a quantity input and an
"Add to cart" button allow the buyer to add the product to the cart.

A reviews section at the bottom lists all reviews for the product with
rating and text.

### Cart (`/cart`)

The page lists all items in the cart. Each row shows the product name, unit
price, an editable quantity input, the row subtotal, and a remove button.
The cart total appears at the bottom right, followed by a "Proceed to
checkout" button.

If the cart is empty, the system displays a message with a link back to the
home page.

### Checkout (`/checkout`)

The page presents a shipping address form with fields for first name, last
name, street, city, ZIP code, and country. Next to the form, a read-only
order summary shows the item list and total.

The buyer confirms the order with a "Confirm order" button. On success, the
system redirects to the order confirmation page.

### Order Confirmation (`/orders/:orderId`)

The page displays a success icon, a confirmation message, the order number,
the current order status as a badge, and the list of ordered items. A
"Continue shopping" button returns the buyer to the home page.

## Agent Notes

- The frontend project lives in `code/frontend/` — separate from `code/src/`
  of the backend
- Initialize with `npm create vite@latest frontend -- --template react`
- Configure the Vite proxy in `vite.config.js` to redirect `/api` to
  `http://localhost:8080`
- Frontend stack: React 18, Vite, React Router v6, React Query
  (TanStack Query), Tailwind CSS, Axios, Keycloak JS
- Folder structure:
  ```
  code/frontend/
    src/
      components/     ← reusable components (ProductCard, CartItem, etc.)
      pages/          ← screens (HomePage, ProductPage, CartPage,
                        CheckoutPage, OrderPage)
      hooks/          ← custom React Query hooks
      api/            ← Axios functions for each endpoint
      auth/           ← Keycloak configuration
    public/
    index.html
    vite.config.js
    package.json
  ```
- Use placeholder images from `https://placehold.co/400x300` for products
- Rating stars: implement with Unicode characters ★/☆ — no external
  libraries
- No UI component library (no MUI, no Ant Design) — Tailwind only
- CORS already configured in the Spring Boot backend for
  `http://localhost:5173`
- All user-facing text must be in English, including placeholders, labels,
  buttons, loading states, error states, empty states, badges, and
  navigation links
- Use English UI copy such as "Search products...", "All categories",
  "Min price", "Search", "Loading...", "No products found.",
  "Discover our products", "Add to cart", "Proceed to checkout",
  "Confirm order", and "Continue shopping"