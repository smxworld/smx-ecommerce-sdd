package com.smxworld.ecommerce.order.internal.application;

import com.smxworld.ecommerce.cart.CartApi;
import com.smxworld.ecommerce.cart.CartItem;
import com.smxworld.ecommerce.order.CreateOrderRequest;
import com.smxworld.ecommerce.order.OrderApi;
import com.smxworld.ecommerce.order.OrderCancelledEvent;
import com.smxworld.ecommerce.order.OrderConfirmedEvent;
import com.smxworld.ecommerce.order.OrderCreatedEvent;
import com.smxworld.ecommerce.order.OrderDetails;
import com.smxworld.ecommerce.order.OrderItem;
import com.smxworld.ecommerce.order.OrderStatus;
import com.smxworld.ecommerce.order.OrderSummary;
import com.smxworld.ecommerce.order.internal.domain.OrderEntity;
import com.smxworld.ecommerce.order.internal.domain.OrderItemEntity;
import com.smxworld.ecommerce.order.internal.infrastructure.OrderRepository;
import com.smxworld.ecommerce.payment.PaymentApi;
import com.smxworld.ecommerce.warehouse.ReservationItem;
import com.smxworld.ecommerce.warehouse.ReservationResult;
import com.smxworld.ecommerce.warehouse.WarehouseApi;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
class OrderService implements OrderApi {

    private final OrderRepository      orderRepo;
    private final CartApi              cartApi;
    private final WarehouseApi         warehouseApi;
    private final PaymentApi           paymentApi;
    private final ApplicationEventPublisher events;

    OrderService(OrderRepository orderRepo,
                 CartApi cartApi,
                 WarehouseApi warehouseApi,
                 PaymentApi paymentApi,
                 ApplicationEventPublisher events) {
        this.orderRepo    = orderRepo;
        this.cartApi      = cartApi;
        this.warehouseApi = warehouseApi;
        this.paymentApi   = paymentApi;
        this.events       = events;
    }

    @Override
    public OrderSummary createOrder(String userId, CreateOrderRequest request) {
        // 1. Resolve items from cart if not provided in the request
        List<OrderItem> orderItems = resolveItems(userId, request);
        if (orderItems.isEmpty()) {
            throw new IllegalArgumentException("Cannot create an order with no items");
        }

        // 2. Create order in PENDING
        OrderEntity order = new OrderEntity(userId, request.shippingAddress());
        orderItems.forEach(item ->
                order.addItem(new OrderItemEntity(
                        item.productId(), item.productName(), item.unitPrice(), item.quantity())));
        orderRepo.save(order);
        events.publishEvent(new OrderCreatedEvent(order.getId(), userId, order.getTotalAmount()));

        // 2. Reserve stock — map to warehouse-owned DTO to avoid a module cycle
        List<ReservationItem> reservationItems = orderItems.stream()
                .map(i -> new ReservationItem(i.productId(), i.quantity()))
                .toList();
        ReservationResult reservation = warehouseApi.reserveStock(order.getId(), reservationItems);
        if (!reservation.successful()) {
            order.transitionTo(OrderStatus.CANCELLED);
            orderRepo.save(order);
            events.publishEvent(new OrderCancelledEvent(order.getId(), userId,
                    "Stock reservation failed: " + reservation.failureReason()));
            return toSummary(order);
        }

        // 3. Process payment
        var paymentResult = paymentApi.processPayment(order.getId(), order.getTotalAmount());
        if (!paymentResult.successful()) {
            warehouseApi.releaseReservation(order.getId());
            order.transitionTo(OrderStatus.CANCELLED);
            orderRepo.save(order);
            events.publishEvent(new OrderCancelledEvent(order.getId(), userId,
                    "Payment failed: " + paymentResult.failureReason()));
            return toSummary(order);
        }

        // 4. Confirm order and clear cart
        order.transitionTo(OrderStatus.CONFIRMED);
        orderRepo.save(order);
        cartApi.clearCart(userId);
        events.publishEvent(new OrderConfirmedEvent(order.getId(), userId, order.getTotalAmount()));
        return toSummary(order);
    }

    // ─── Private helpers ──────────────────────────────────────────────────────

    private List<OrderItem> resolveItems(String userId, CreateOrderRequest request) {
        if (request.items() != null && !request.items().isEmpty()) {
            return request.items();
        }
        return cartApi.getCartItems(userId).stream()
                .map(ci -> new OrderItem(ci.productId(), ci.productName(), ci.quantity(), ci.unitPrice()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDetails getOrder(UUID orderId) {
        OrderEntity order = findOrder(orderId);
        return toDetails(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderSummary> getOrdersByUser(String userId) {
        return orderRepo.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toSummary).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasDeliveredOrderWithProduct(String userId, UUID productId) {
        return orderRepo.existsByUserIdAndItemProductIdAndStatus(userId, productId, OrderStatus.DELIVERED);
    }

    @Override
    public void updateStatus(UUID orderId, OrderStatus status) {
        OrderEntity order = findOrder(orderId);
        order.transitionTo(status);
        orderRepo.save(order);
    }

    // ─── Private helpers ──────────────────────────────────────────────────────

    private OrderEntity findOrder(UUID orderId) {
        return orderRepo.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }

    private OrderSummary toSummary(OrderEntity o) {
        return new OrderSummary(o.getId(), o.getUserId(), o.getStatus(), o.getTotalAmount(), o.getCreatedAt());
    }

    private OrderDetails toDetails(OrderEntity o) {
        List<OrderItem> items = o.getItems().stream()
                .map(i -> new OrderItem(i.getProductId(), i.getProductName(), i.getQuantity(), i.getUnitPrice()))
                .toList();
        return new OrderDetails(o.getId(), o.getUserId(), o.getStatus(), items,
                o.getTotalAmount(), o.getCreatedAt(), o.getUpdatedAt());
    }
}
