package com.smxworld.ecommerce.cart.internal.repository;

import com.smxworld.ecommerce.cart.internal.model.CartEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartEntity, UUID> {
    Optional<CartEntity> findByUserId(String userId);
}
