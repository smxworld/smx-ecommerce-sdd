---
title: "Interfaces"
status: synced
author: ""
last-modified: "2026-04-10T00:00:00.000Z"
version: "2.0"
---

# Interfaces

Definizione dei contratti tra moduli: API pubbliche Java e eventi di dominio.

L'unica API REST esposta verso l'esterno è quella del modulo `api` (porta 8080).

---

## API REST esterna (modulo `api` — porta 8080)

Tutti gli endpoint richiedono JWT valido (`Authorization: Bearer <token>`).

| Metodo | Path | Modulo delegato | Descrizione |
|---|---|---|---|
| `GET` | `/api/search` | `CatalogApi` | Ricerca prodotti |
| `GET` | `/api/products/{id}` | `CatalogApi` + `WarehouseApi` | Dettaglio prodotto |
| `GET` | `/api/cart` | `CartApi` | Carrello utente corrente |
| `POST` | `/api/cart/items` | `CartApi` | Aggiungi item |
| `PUT` | `/api/cart/items/{productId}` | `CartApi` | Modifica quantità |
| `DELETE` | `/api/cart/items/{productId}` | `CartApi` | Rimuovi item |
| `POST` | `/api/checkout` | `OrderApi` | Avvia checkout |
| `GET` | `/api/orders` | `OrderApi` | Lista ordini utente |
| `GET` | `/api/orders/{orderId}` | `OrderApi` | Dettaglio ordine |
| `GET` | `/api/shipments/{orderId}` | `ShipmentApi` | Tracking spedizione |
| `GET` | `/api/reviews/{productId}` | `ReviewApi` | Recensioni prodotto |
| `POST` | `/api/reviews` | `ReviewApi` | Crea recensione |
| `PUT` | `/api/orders/{orderId}/status` | `OrderApi` | Aggiorna stato (backoffice) |
| `PUT` | `/api/warehouse/products/{id}/stock` | `WarehouseApi` | Aggiorna stock (backoffice) |

---

## API Pubbliche Java tra moduli

Ogni modulo espone una sola interfaccia pubblica. Gli altri moduli (e il modulo `api`) chiamano solo questi metodi — mai le classi `internal/`.

### CatalogApi

```java
public interface CatalogApi {
    ProductDetails getProduct(UUID productId);
    SearchResult search(SearchQuery query);
    void updateProductScore(UUID productId, double score); // chiamato da analytics
}
```

### CartApi

```java
public interface CartApi {
    Cart getCart(String userId);
    Cart addItem(String userId, UUID productId, int quantity);
    Cart updateItem(String userId, UUID productId, int quantity);
    Cart removeItem(String userId, UUID productId);
    void clearCart(String userId);
    List<CartItem> getCartItems(String userId); // chiamato da order al checkout
}
```

### OrderApi

```java
public interface OrderApi {
    OrderSummary createOrder(String userId, CreateOrderRequest request);
    OrderDetails getOrder(UUID orderId);
    List<OrderSummary> getOrdersByUser(String userId);
    boolean hasDeliveredOrderWithProduct(String userId, UUID productId); // chiamato da review
    void updateStatus(UUID orderId, OrderStatus status); // backoffice
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
    void updateStock(UUID productId, int quantity); // backoffice
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
    // Non espone metodi pubblici diretti — reagisce solo a eventi
}
```

### AnalyticsApi

```java
public interface AnalyticsApi {
    // Non espone metodi pubblici diretti — reagisce solo a eventi
}
```

---

## Eventi di dominio (Spring Application Events)

Tutti gli eventi sono Java `record` immutabili. Vengono pubblicati con `ApplicationEventPublisher` e consumati con `@ApplicationModuleListener`.

| Evento | Produttore | Consumatori | Campi principali |
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

### Nota sulla migrazione a Kafka

Quando un evento deve essere esternalizzato (es. al momento dell'estrazione di un modulo a microservizio), è sufficiente:

```java
@Externalized("smx.order-confirmed")  // aggiungere questa annotazione
public record OrderConfirmedEvent(UUID orderId, String userId, BigDecimal totalAmount) {}
```

Produttori e consumatori non cambiano.

---

## Agent Notes

- Le interfacce `*Api.java` vanno nel package root del modulo, non in `internal/`
- Gli eventi vanno nel package root del modulo in un file `<Module>Events.java` o come classi separate
- I DTO di ritorno (es. `ProductDetails`, `OrderSummary`) vanno anch'essi nel package root — sono parte del contratto pubblico
- Mai esporre entità JPA come tipo di ritorno delle API pubbliche — usare sempre DTO
- `@ApplicationModuleListener` è l'annotazione di Spring Modulith per i consumer di eventi — gestisce automaticamente la transazionalità
- La verifica dei confini si fa con: `ApplicationModules.of(SmxECommerceApplication.class).verify()`
