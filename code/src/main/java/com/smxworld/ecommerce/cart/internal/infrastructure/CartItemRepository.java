package com.smxworld.ecommerce.cart.internal.infrastructure;

import com.smxworld.ecommerce.cart.internal.domain.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItemEntity, UUID> {}
