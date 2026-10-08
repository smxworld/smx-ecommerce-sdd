package com.smxworld.ecommerce.review.internal.repository;

import com.smxworld.ecommerce.review.internal.model.ReviewEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewEntity, UUID> {
    List<ReviewEntity> findByProductIdOrderByCreatedAtDesc(UUID productId);
}
