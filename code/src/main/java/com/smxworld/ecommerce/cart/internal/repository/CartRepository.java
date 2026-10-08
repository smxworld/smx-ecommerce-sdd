package com.smxworld.ecommerce.cart.internal.repository;

import com.smxworld.ecommerce.cart.internal.model.CartEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for {@link CartEntity}.
 *
 * <p>Each buyer has one cart, looked up by user id, so that it survives across sessions. See product/features/cart.md.
 */
public interface CartRepository extends JpaRepository<CartEntity, UUID> {
    Optional<CartEntity> findByUserId(String userId);
}
