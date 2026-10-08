package com.smxworld.ecommerce.payment.internal.repository;

import com.smxworld.ecommerce.payment.internal.model.PaymentEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for {@link PaymentEntity}.
 *
 * <p>The lookup by order id is what makes payment processing idempotent: a repeated call for the same order finds the
 * previous payment and returns its result instead of charging again. See product/features/payment.md.
 */
public interface PaymentRepository extends JpaRepository<PaymentEntity, UUID> {
    Optional<PaymentEntity> findByOrderId(UUID orderId);
}
