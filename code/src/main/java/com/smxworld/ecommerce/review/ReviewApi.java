package com.smxworld.ecommerce.review;

import java.util.List;
import java.util.UUID;

/**
 * Public API of the Review module.
 */
public interface ReviewApi {

    List<Review> getReviews(UUID productId);

    Review createReview(String userId, UUID productId, UUID orderId, int rating, String text);
}
