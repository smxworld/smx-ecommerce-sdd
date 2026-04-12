package com.smxworld.ecommerce.payment.internal.infrastructure;

import com.smxworld.ecommerce.payment.internal.domain.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface PaymentRepository extends JpaRepository<PaymentEntity, UUID> {
    Optional<PaymentEntity> findByOrderId(UUID orderId);
}
