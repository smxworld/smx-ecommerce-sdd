---
title: "Interfaces"
status: synced
author: ""
last-modified: "2026-04-10T00:00:00.000Z"
version: "2.0"
---

# Interfaces

Definition of contracts between modules: public Java APIs and domain events.

The only REST API exposed to the outside is the one from the `api` module (port 8080).

---

## External REST API (`api` module — port 8080)

All endpoints require a valid JWT (`Authorization: Bearer <token>`).

| Method | Path | Delegated module | Description |
|---|---|---|---|
| `GET` | `/api/search` | `CatalogApi` | Product search |
| `GET` | `/api/products/{id}` | `CatalogApi` + `WarehouseApi` | Product detail |
| `GET` | `/api/cart` | `CartApi` | Current user's cart |
| `POST` | `/api/cart/items` | `CartApi` | Add item |
| `PUT` | `/api/cart/items/{productId}` | `CartApi` | Update quantity |
| `DELETE` | `/api/cart/items/{productId}` | `CartApi` | Remove item |
| `POST` | `/api/checkout` | `OrderApi` | Start checkout |
| `GET` | `/api/orders` | `OrderApi` | User order list |
| `GET` | `/api/orders/{orderId}` | `OrderApi` | Order detail |
| `GET` | `/api/shipments/{orderId}` | `ShipmentApi` | Shipment tracking |
| `GET` | `/api/reviews/{productId}` | `ReviewApi` | Product reviews |
| `POST` | `/api/reviews` | `ReviewApi` | Create review |
| `PUT` | `/api/orders/{orderId}/status` | `OrderApi` | Update status (back-office) |
| `PUT` | `/api/warehouse/products/{id}/stock` | `WarehouseApi` | Update stock (back-office) |

---

## Public Java APIs between modules

Each module exposes a single public interface. Other modules (and the `api` module) call only these methods — never `internal/` classes.

### CatalogApi

```java
public interface CatalogApi {
    ProductDetails getProduct(UUID productId);
    SearchResult search(SearchQuery query);
    void updateProductScore(UUID productId, double score); // called by analytics
}
```

### CartApi

```java
public interface CartApi {
    Cart getCart(String userId);
    Cart addItem(String userId, UUID productId, String productName, BigDecimal unitPrice, int quantity);
    Cart updateItem(String userId, UUID productId, int quantity);
    Cart removeItem(String userId, UUID productId);
    void clearCart(String userId);
    List<CartItem> getCartItems(String userId); // called by order at checkout
}
```

### OrderApi

```java
public interface OrderApi {
    OrderSummary createOrder(String userId, CreateOrderRequest request);
    OrderDetails getOrder(UUID orderId);
    List<OrderSummary> getOrdersByUser(String userId);
    boolean hasDeliveredOrderWithProduct(String userId, UUID productId); // called by review
    void updateStatus(UUID orderId, OrderStatus status); // back-office
}
```

### PaymentApi

```java
public interface PaymentApi {
    PaymentResult processPayment(UUID orderId, BigDecimal amount);
}
```

### WarehouseApi

```java
public interface WarehouseApi {
    StockInfo getStock(UUID productId);
    ReservationResult reserveStock(UUID orderId, List<OrderItem> items);
    void releaseReservation(UUID orderId);
    void updateStock(UUID productId, int quantity); // back-office
}
```

### ShipmentApi

```java
public interface ShipmentApi {
    ShipmentInfo getShipment(UUID orderId);
}
```

### ReviewApi

```java
public interface ReviewApi {
    List<Review> getReviews(UUID productId);
    Review createReview(String userId, UUID productId, UUID orderId, int rating, String text);
}
```

### NotificationApi

```java
public interface NotificationApi {
    // No direct public methods — reacts to events only
}
```

### AnalyticsApi

```java
public interface AnalyticsApi {
    // No direct public methods — reacts to events only
}
```

---

## Domain events (Spring Application Events)

All events are immutable Java `record`s. They are published with `ApplicationEventPublisher` and consumed with `@ApplicationModuleListener`.

| Event | Producer | Consumers | Main fields |
|---|---|---|---|
| `ProductBookedEvent` | `cart` | `warehouse` | `productId, userId, quantity` |
| `ProductUnbookedEvent` | `cart` | `warehouse` | `productId, userId, quantity` |
| `SearchPerformedEvent` | `catalog` | `analytics` | `userId, query, resultsCount` |
| `OrderCreatedEvent` | `order` | `notification` | `orderId, userId, totalAmount` |
| `OrderConfirmedEvent` | `order` | `notification` | `orderId, userId, totalAmount` |
| `OrderCancelledEvent` | `order` | `warehouse`, `notification` | `orderId, userId, reason` |
| `PaymentSucceededEvent` | `payment` | `order` | `orderId, transactionId, amount` |
| `PaymentFailedEvent` | `payment` | `order`, `notification` | `orderId, reason` |
| `StockReservedEvent` | `warehouse` | `order` | `orderId, confirmed: true` |
| `StockReservationFailedEvent` | `warehouse` | `order` | `orderId, reason` |
| `OrderShippedEvent` | `shipment` | `notification` | `orderId, userId, trackingNumber` |
| `ReviewCreatedEvent` | `review` | `catalog` | `productId, rating` |
| `SearchScoreUpdatedEvent` | `analytics` | `catalog` | `productId, newScore` |

### Note on migration to Kafka

When an event needs to be externalized (e.g., when extracting a module into a microservice), it is sufficient to:

```java
@Externalized("smx.order-confirmed")  // add this annotation
public record OrderConfirmedEvent(UUID orderId, String userId, BigDecimal totalAmount) {}
```

Producers and consumers do not change.

---

## Agent Notes

- The `*Api.java` interfaces go in the module's root package, not in `internal/`
- Events go in the module's root package in a `<Module>Events.java` file or as separate classes
- Return DTOs (e.g., `ProductDetails`, `OrderSummary`) also go in the root package — they are part of the public contract
- Never expose JPA entities as return types of public APIs — always use DTOs
- `@ApplicationModuleListener` is the Spring Modulith annotation for event consumers — handles transactionality automatically
- Boundary verification is done with: `ApplicationModules.of(SmxECommerceApplication.class).verify()`
