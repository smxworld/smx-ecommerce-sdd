package com.smxworld.ecommerce.warehouse.internal.application;

import com.smxworld.ecommerce.cart.ProductBookedEvent;
import com.smxworld.ecommerce.cart.ProductUnbookedEvent;
import com.smxworld.ecommerce.order.OrderCancelledEvent;
import com.smxworld.ecommerce.order.OrderItem;
import com.smxworld.ecommerce.warehouse.ReservationResult;
import com.smxworld.ecommerce.warehouse.StockInfo;
import com.smxworld.ecommerce.warehouse.StockReservationFailedEvent;
import com.smxworld.ecommerce.warehouse.StockReservedEvent;
import com.smxworld.ecommerce.warehouse.WarehouseApi;
import com.smxworld.ecommerce.warehouse.internal.domain.StockEntity;
import com.smxworld.ecommerce.warehouse.internal.infrastructure.StockRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
class WarehouseService implements WarehouseApi {

    private final StockRepository stockRepo;
    private final ApplicationEventPublisher events;

    WarehouseService(StockRepository stockRepo, ApplicationEventPublisher events) {
        this.stockRepo = stockRepo;
        this.events    = events;
    }

    @Override
    @Transactional(readOnly = true)
    public StockInfo getStock(UUID productId) {
        return stockRepo.findByProductId(productId)
                .map(s -> new StockInfo(productId, s.availableQuantity(), s.getQuantityReserved()))
                .orElse(new StockInfo(productId, 0, 0));
    }

    @Override
    public ReservationResult reserveStock(UUID orderId, List<OrderItem> items) {
        for (OrderItem item : items) {
            StockEntity stock = stockRepo.findByProductId(item.productId())
                    .orElse(null);
            if (stock == null || !stock.reserve(item.quantity())) {
                String reason = stock == null
                        ? "No stock record for product: " + item.productId()
                        : "Insufficient stock for product: " + item.productId();
                events.publishEvent(new StockReservationFailedEvent(orderId, reason));
                return new ReservationResult(orderId, false, reason);
            }
            stockRepo.save(stock);
        }
        events.publishEvent(new StockReservedEvent(orderId, true));
        return new ReservationResult(orderId, true, null);
    }

    @Override
    public void releaseReservation(UUID orderId) {
        // Without a reservation ledger keyed by orderId, we can't easily reverse.
        // In a real system, a StockReservation join table would track per-order reservations.
        // This is a no-op placeholder consistent with the simulated gateway approach.
    }

    @Override
    public void updateStock(UUID productId, int quantity) {
        StockEntity stock = stockRepo.findByProductId(productId)
                .orElseGet(() -> new StockEntity(productId, 0));
        stock.adjustTotal(quantity);
        stockRepo.save(stock);
    }

    // ─── Event listeners ──────────────────────────────────────────────────────

    @ApplicationModuleListener
    void on(ProductBookedEvent event) {
        StockEntity stock = stockRepo.findByProductId(event.productId())
                .orElseGet(() -> new StockEntity(event.productId(), 0));
        stock.reserve(event.quantity());
        stockRepo.save(stock);
    }

    @ApplicationModuleListener
    void on(ProductUnbookedEvent event) {
        stockRepo.findByProductId(event.productId()).ifPresent(stock -> {
            stock.release(event.quantity());
            stockRepo.save(stock);
        });
    }

    @ApplicationModuleListener
    void on(OrderCancelledEvent event) {
        // Release reservations on order cancellation.
        // In a production system this would use a reservation ledger.
        // Here it's a signal that future orders may use the previously reserved stock.
    }
}
