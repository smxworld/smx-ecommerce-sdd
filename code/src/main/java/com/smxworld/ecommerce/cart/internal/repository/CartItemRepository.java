package com.smxworld.ecommerce.cart.internal.repository;

import com.smxworld.ecommerce.cart.internal.model.CartItemEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for {@link CartItemEntity}.
 *
 * <p>Items are normally reached through their {@link CartEntity}; the repository exists for operations on a single
 * line. See product/features/cart.md.
 */
public interface CartItemRepository extends JpaRepository<CartItemEntity, UUID> {}
