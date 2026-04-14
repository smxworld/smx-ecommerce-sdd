package com.smxworld.ecommerce.api.rest;

import com.smxworld.ecommerce.catalog.CatalogApi;
import com.smxworld.ecommerce.catalog.ProductDetails;
import com.smxworld.ecommerce.catalog.SearchQuery;
import com.smxworld.ecommerce.catalog.SearchResult;
import com.smxworld.ecommerce.cart.Cart;
import com.smxworld.ecommerce.cart.CartApi;
import com.smxworld.ecommerce.order.CreateOrderRequest;
import com.smxworld.ecommerce.order.OrderApi;
import com.smxworld.ecommerce.order.OrderDetails;
import com.smxworld.ecommerce.order.OrderStatus;
import com.smxworld.ecommerce.order.OrderSummary;
import com.smxworld.ecommerce.review.Review;
import com.smxworld.ecommerce.review.ReviewApi;
import com.smxworld.ecommerce.shipment.ShipmentApi;
import com.smxworld.ecommerce.shipment.ShipmentInfo;
import com.smxworld.ecommerce.warehouse.StockInfo;
import com.smxworld.ecommerce.warehouse.WarehouseApi;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
class ECommerceController {

    private final CatalogApi catalogApi;
    private final CartApi cartApi;
    private final OrderApi orderApi;
    private final WarehouseApi warehouseApi;
    private final ShipmentApi shipmentApi;
    private final ReviewApi reviewApi;

    ECommerceController(
            CatalogApi catalogApi,
            CartApi cartApi,
            OrderApi orderApi,
            WarehouseApi warehouseApi,
            ShipmentApi shipmentApi,
            ReviewApi reviewApi) {
        this.catalogApi = catalogApi;
        this.cartApi = cartApi;
        this.orderApi = orderApi;
        this.warehouseApi = warehouseApi;
        this.shipmentApi = shipmentApi;
        this.reviewApi = reviewApi;
    }

    // ─── Catalog ──────────────────────────────────────────────────────────────

    @GetMapping("/search")
    ResponseEntity<SearchResultView> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) java.math.BigDecimal minPrice,
            @RequestParam(required = false) java.math.BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        SearchResult result = catalogApi.search(new SearchQuery(q, category, minPrice, maxPrice, page, size));
        return ResponseEntity.ok(new SearchResultView(
                result.items().stream().map(this::toProductView).toList(),
                result.totalElements(),
                result.totalPages()));
    }

    @GetMapping("/products/{id}")
    ResponseEntity<ProductView> getProduct(@PathVariable UUID id) {
        ProductDetails product = catalogApi.getProduct(id);
        return ResponseEntity.ok(toProductView(product));
    }

    // ─── Cart ─────────────────────────────────────────────────────────────────

    @GetMapping("/cart")
    ResponseEntity<Cart> getCart(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(cartApi.getCart(jwt.getSubject()));
    }

    @PostMapping("/cart/items")
    ResponseEntity<Cart> addItem(@AuthenticationPrincipal Jwt jwt,
                                 @RequestParam UUID productId,
                                 @RequestParam int quantity) {
        return ResponseEntity.ok(cartApi.addItem(jwt.getSubject(), productId, quantity));
    }

    @PutMapping("/cart/items/{productId}")
    ResponseEntity<Cart> updateItem(@AuthenticationPrincipal Jwt jwt,
                                    @PathVariable UUID productId,
                                    @RequestParam int quantity) {
        return ResponseEntity.ok(cartApi.updateItem(jwt.getSubject(), productId, quantity));
    }

    @DeleteMapping("/cart/items/{productId}")
    ResponseEntity<Cart> removeItem(@AuthenticationPrincipal Jwt jwt,
                                    @PathVariable UUID productId) {
        return ResponseEntity.ok(cartApi.removeItem(jwt.getSubject(), productId));
    }

    // ─── Order ────────────────────────────────────────────────────────────────

    @PostMapping("/checkout")
    ResponseEntity<OrderSummary> checkout(@AuthenticationPrincipal Jwt jwt,
                                          @RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(orderApi.createOrder(jwt.getSubject(), request));
    }

    @GetMapping("/orders")
    ResponseEntity<List<OrderSummary>> getOrders(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(orderApi.getOrdersByUser(jwt.getSubject()));
    }

    @GetMapping("/orders/{orderId}")
    ResponseEntity<OrderDetails> getOrder(@PathVariable UUID orderId) {
        return ResponseEntity.ok(orderApi.getOrder(orderId));
    }

    @PutMapping("/orders/{orderId}/status")
    ResponseEntity<Void> updateOrderStatus(@PathVariable UUID orderId,
                                           @RequestBody OrderStatus status) {
        orderApi.updateStatus(orderId, status);
        return ResponseEntity.noContent().build();
    }

    // ─── Shipment ─────────────────────────────────────────────────────────────

    @GetMapping("/shipments/{orderId}")
    ResponseEntity<ShipmentInfo> getShipment(@PathVariable UUID orderId) {
        return ResponseEntity.ok(shipmentApi.getShipment(orderId));
    }

    // ─── Reviews ──────────────────────────────────────────────────────────────

    @GetMapping("/reviews/{productId}")
    ResponseEntity<List<Review>> getReviews(@PathVariable UUID productId) {
        return ResponseEntity.ok(reviewApi.getReviews(productId));
    }

    @PostMapping("/reviews")
    ResponseEntity<Review> createReview(@AuthenticationPrincipal Jwt jwt,
                                        @RequestParam UUID productId,
                                        @RequestParam UUID orderId,
                                        @RequestParam int rating,
                                        @RequestParam String text) {
        return ResponseEntity.ok(
                reviewApi.createReview(jwt.getSubject(), productId, orderId, rating, text));
    }

    // ─── Backoffice ───────────────────────────────────────────────────────────

    @PutMapping("/warehouse/products/{id}/stock")
    ResponseEntity<Void> updateStock(@PathVariable UUID id,
                                     @RequestParam int quantity) {
        warehouseApi.updateStock(id, quantity);
        return ResponseEntity.noContent().build();
    }

    // ─── Internal response wrappers ───────────────────────────────────────────

    private ProductView toProductView(ProductDetails product) {
        StockInfo stock = warehouseApi.getStock(product.id());
        return new ProductView(
                product.id(),
                product.name(),
                product.description(),
                product.price(),
                product.category(),
                product.averageRating(),
                product.searchScore(),
                stock.availableQuantity());
    }

    record SearchResultView(List<ProductView> items, long totalElements, int totalPages) {}

    record ProductView(
            UUID id,
            String name,
            String description,
            java.math.BigDecimal price,
            String category,
            double averageRating,
            double searchScore,
            int stockAvailable
    ) {}
}
