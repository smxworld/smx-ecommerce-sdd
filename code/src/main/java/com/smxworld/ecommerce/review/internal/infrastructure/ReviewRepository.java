package com.smxworld.ecommerce.review.internal.infrastructure;

import com.smxworld.ecommerce.review.internal.domain.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface ReviewRepository extends JpaRepository<ReviewEntity, UUID> {
    List<ReviewEntity> findByProductIdOrderByCreatedAtDesc(UUID productId);
}
