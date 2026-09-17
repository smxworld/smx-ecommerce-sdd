package com.smxworld.ecommerce.cart.internal.repository;

import com.smxworld.ecommerce.cart.internal.model.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItemEntity, UUID> {}
