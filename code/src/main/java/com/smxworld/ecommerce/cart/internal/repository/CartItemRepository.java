package com.smxworld.ecommerce.cart.internal.repository;

import com.smxworld.ecommerce.cart.internal.model.CartItemEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItemEntity, UUID> {}
