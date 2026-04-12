package com.smxworld.ecommerce.review.internal.application;

import com.smxworld.ecommerce.order.OrderApi;
import com.smxworld.ecommerce.review.Review;
import com.smxworld.ecommerce.review.ReviewApi;
import com.smxworld.ecommerce.review.ReviewCreatedEvent;
import com.smxworld.ecommerce.review.internal.domain.ReviewEntity;
import com.smxworld.ecommerce.review.internal.infrastructure.ReviewRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
class ReviewService implements ReviewApi {

    private final ReviewRepository reviewRepo;
    private final OrderApi orderApi;
    private final ApplicationEventPublisher events;

    ReviewService(ReviewRepository reviewRepo, OrderApi orderApi, ApplicationEventPublisher events) {
        this.reviewRepo = reviewRepo;
        this.orderApi   = orderApi;
        this.events     = events;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getReviews(UUID productId) {
        return reviewRepo.findByProductIdOrderByCreatedAtDesc(productId)
                .stream().map(this::toDto).toList();
    }

    @Override
    public Review createReview(String userId, UUID productId, UUID orderId, int rating, String text) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        if (!orderApi.hasDeliveredOrderWithProduct(userId, productId)) {
            throw new IllegalStateException("User has no delivered order containing product: " + productId);
        }

        ReviewEntity review = new ReviewEntity(productId, userId, orderId, rating, text);
        ReviewEntity saved  = reviewRepo.save(review);
        events.publishEvent(new ReviewCreatedEvent(productId, rating));
        return toDto(saved);
    }

    private Review toDto(ReviewEntity e) {
        return new Review(e.getId(), e.getProductId(), e.getUserId(), e.getRating(), e.getText(), e.getCreatedAt());
    }
}
