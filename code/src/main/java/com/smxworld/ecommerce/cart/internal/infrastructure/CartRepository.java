package com.smxworld.ecommerce.cart.internal.infrastructure;

import com.smxworld.ecommerce.cart.internal.domain.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<CartEntity, UUID> {
    Optional<CartEntity> findByUserId(String userId);
}
