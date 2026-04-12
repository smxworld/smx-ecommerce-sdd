package com.smxworld.ecommerce.review;

import java.time.Instant;
import java.util.UUID;

/** DTO — public contract of the Review module. */
public record Review(
        UUID reviewId,
        UUID productId,
        String userId,
        int rating,
        String text,
        Instant createdAt
) {}
