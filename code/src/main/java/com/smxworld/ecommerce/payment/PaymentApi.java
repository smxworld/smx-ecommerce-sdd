package com.smxworld.ecommerce.payment;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Public API of the Payment module.
 */
public interface PaymentApi {

    PaymentResult processPayment(UUID orderId, BigDecimal amount);
}
