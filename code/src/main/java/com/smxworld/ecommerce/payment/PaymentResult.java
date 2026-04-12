package com.smxworld.ecommerce.payment;

import java.math.BigDecimal;
import java.util.UUID;

/** DTO — result of a payment processing attempt. */
public record PaymentResult(
        UUID orderId,
        String transactionId,
        boolean successful,
        String failureReason,
        BigDecimal amount
) {}
