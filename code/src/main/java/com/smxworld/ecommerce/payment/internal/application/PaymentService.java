package com.smxworld.ecommerce.payment.internal.application;

import com.smxworld.ecommerce.payment.PaymentApi;
import com.smxworld.ecommerce.payment.PaymentFailedEvent;
import com.smxworld.ecommerce.payment.PaymentResult;
import com.smxworld.ecommerce.payment.PaymentSucceededEvent;
import com.smxworld.ecommerce.payment.internal.domain.PaymentEntity;
import com.smxworld.ecommerce.payment.internal.infrastructure.PaymentRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Simulated payment gateway: accepts all payments with amount < 10,000.
 * Idempotent: if orderId already processed, returns previous result.
 */
@Service
@Transactional
class PaymentService implements PaymentApi {

    private static final BigDecimal MAX_AMOUNT = new BigDecimal("9999.99");

    private final PaymentRepository paymentRepo;
    private final ApplicationEventPublisher events;

    PaymentService(PaymentRepository paymentRepo, ApplicationEventPublisher events) {
        this.paymentRepo = paymentRepo;
        this.events      = events;
    }

    @Override
    public PaymentResult processPayment(UUID orderId, BigDecimal amount) {
        // Idempotency: return existing result if already processed
        return paymentRepo.findByOrderId(orderId)
                .map(existing -> toResult(existing))
                .orElseGet(() -> process(orderId, amount));
    }

    private PaymentResult process(UUID orderId, BigDecimal amount) {
        PaymentEntity payment = new PaymentEntity(orderId, amount);

        if (amount.compareTo(MAX_AMOUNT) > 0) {
            payment.markFailed("Amount exceeds maximum allowed: " + MAX_AMOUNT);
            paymentRepo.save(payment);
            events.publishEvent(new PaymentFailedEvent(orderId, payment.getFailureReason()));
            return toResult(payment);
        }

        String transactionId = UUID.randomUUID().toString();
        payment.markSuccess(transactionId);
        paymentRepo.save(payment);
        events.publishEvent(new PaymentSucceededEvent(orderId, transactionId, amount));
        return toResult(payment);
    }

    private PaymentResult toResult(PaymentEntity p) {
        return new PaymentResult(
                p.getOrderId(),
                p.getTransactionId(),
                "SUCCESS".equals(p.getStatus()),
                p.getFailureReason(),
                p.getAmount());
    }
}
