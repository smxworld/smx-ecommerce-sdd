package com.smxworld.ecommerce.payment;

import java.math.BigDecimal;
import java.util.UUID;

/** Domain event published by Payment on success. Consumed by Order. */
public record PaymentSucceededEvent(
        UUID orderId,
        String transactionId,
        BigDecimal amount
) {}
