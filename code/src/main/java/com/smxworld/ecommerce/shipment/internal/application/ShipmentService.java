package com.smxworld.ecommerce.shipment.internal.application;

import com.smxworld.ecommerce.order.OrderConfirmedEvent;
import com.smxworld.ecommerce.shipment.OrderShippedEvent;
import com.smxworld.ecommerce.shipment.ShipmentApi;
import com.smxworld.ecommerce.shipment.ShipmentInfo;
import com.smxworld.ecommerce.shipment.internal.domain.ShipmentEntity;
import com.smxworld.ecommerce.shipment.internal.infrastructure.ShipmentRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
class ShipmentService implements ShipmentApi {

    private final ShipmentRepository shipmentRepo;
    private final ApplicationEventPublisher events;

    ShipmentService(ShipmentRepository shipmentRepo, ApplicationEventPublisher events) {
        this.shipmentRepo = shipmentRepo;
        this.events       = events;
    }

    @Override
    @Transactional(readOnly = true)
    public ShipmentInfo getShipment(UUID orderId) {
        return shipmentRepo.findByOrderId(orderId)
                .map(this::toInfo)
                .orElseThrow(() -> new IllegalArgumentException("Shipment not found for order: " + orderId));
    }

    // ─── Event listeners ──────────────────────────────────────────────────────

    /**
     * When an order is confirmed, immediately create a shipment record
     * and publish OrderShippedEvent (simulated carrier).
     */
    @ApplicationModuleListener
    void on(OrderConfirmedEvent event) {
        if (shipmentRepo.findByOrderId(event.orderId()).isPresent()) return;

        ShipmentEntity shipment = new ShipmentEntity(event.orderId(), event.userId());
        shipmentRepo.save(shipment);
        events.publishEvent(new OrderShippedEvent(
                event.orderId(), event.userId(), shipment.getTrackingNumber()));
    }

    // ─── Private helpers ──────────────────────────────────────────────────────

    private ShipmentInfo toInfo(ShipmentEntity s) {
        return new ShipmentInfo(
                s.getOrderId(),
                s.getTrackingNumber(),
                s.getCarrier(),
                s.getStatus(),
                s.getEstimatedDelivery(),
                s.getShippedAt());
    }
}
