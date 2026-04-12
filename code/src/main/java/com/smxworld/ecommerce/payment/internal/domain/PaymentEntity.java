package com.smxworld.ecommerce.payment.internal.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payments", schema = "smx_payment")
public class PaymentEntity {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "order_id", nullable = false, unique = true)
    private UUID orderId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(name = "transaction_id", length = 255)
    private String transactionId;

    @Column(name = "failure_reason", length = 255)
    private String failureReason;

    @Column(name = "processed_at", nullable = false)
    private Instant processedAt;

    protected PaymentEntity() {}

    public PaymentEntity(UUID orderId, BigDecimal amount) {
        this.orderId     = orderId;
        this.amount      = amount;
        this.status      = "PENDING";
        this.processedAt = Instant.now();
    }

    public void markSuccess(String transactionId) {
        this.status        = "SUCCESS";
        this.transactionId = transactionId;
        this.processedAt   = Instant.now();
    }

    public void markFailed(String reason) {
        this.status        = "FAILED";
        this.failureReason = reason;
        this.processedAt   = Instant.now();
    }

    public UUID getId()              { return id; }
    public UUID getOrderId()         { return orderId; }
    public BigDecimal getAmount()    { return amount; }
    public String getStatus()        { return status; }
    public String getTransactionId() { return transactionId; }
    public String getFailureReason() { return failureReason; }
    public Instant getProcessedAt()  { return processedAt; }
}
