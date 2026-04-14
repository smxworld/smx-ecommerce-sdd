package com.smxworld.ecommerce.cart.internal.application;

import com.smxworld.ecommerce.cart.Cart;
import com.smxworld.ecommerce.cart.CartApi;
import com.smxworld.ecommerce.cart.CartItem;
import com.smxworld.ecommerce.cart.ProductBookedEvent;
import com.smxworld.ecommerce.cart.ProductUnbookedEvent;
import com.smxworld.ecommerce.cart.internal.domain.CartEntity;
import com.smxworld.ecommerce.cart.internal.domain.CartItemEntity;
import com.smxworld.ecommerce.cart.internal.infrastructure.CartRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
class CartService implements CartApi {

    private final CartRepository cartRepo;
    private final ApplicationEventPublisher events;

    CartService(CartRepository cartRepo, ApplicationEventPublisher events) {
        this.cartRepo = cartRepo;
        this.events   = events;
    }

    @Override
    @Transactional(readOnly = true)
    public Cart getCart(String userId) {
        return cartRepo.findByUserId(userId)
                .map(this::toDto)
                .orElse(new Cart(userId, List.of(), BigDecimal.ZERO));
    }

    @Override
    public Cart addItem(String userId, UUID productId, int quantity) {
        validateQuantity(quantity);
        CartEntity cart = cartRepo.findByUserId(userId)
                .orElseGet(() -> cartRepo.save(new CartEntity(userId)));
        if (productId == null) {
            throw new IllegalArgumentException("productId must be provided");
        }

        cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .ifPresentOrElse(
                        existing -> existing.setQuantity(existing.getQuantity() + quantity),
                        () -> cart.addItem(new CartItemEntity(productId, "Product-" + productId, quantity, BigDecimal.ONE))
                );

        CartEntity saved = cartRepo.save(cart);
        events.publishEvent(new ProductBookedEvent(productId, userId, quantity));
        return toDto(saved);
    }

    @Override
    public Cart updateItem(String userId, UUID productId, int quantity) {
        validateQuantity(quantity);
        CartEntity cart = getCartEntity(userId);

        CartItemEntity item = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not in cart: " + productId));

        int delta = quantity - item.getQuantity();
        item.setQuantity(quantity);

        CartEntity saved = cartRepo.save(cart);

        if (delta > 0) {
            events.publishEvent(new ProductBookedEvent(productId, userId, delta));
        } else if (delta < 0) {
            events.publishEvent(new ProductUnbookedEvent(productId, userId, -delta));
        }

        return toDto(saved);
    }

    @Override
    public Cart removeItem(String userId, UUID productId) {
        CartEntity cart = getCartEntity(userId);

        CartItemEntity item = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not in cart: " + productId));

        int removedQty = item.getQuantity();
        cart.removeItem(item);

        CartEntity saved = cartRepo.save(cart);
        events.publishEvent(new ProductUnbookedEvent(productId, userId, removedQty));
        return toDto(saved);
    }

    @Override
    public void clearCart(String userId) {
        cartRepo.findByUserId(userId).ifPresent(cart -> {
            cart.getItems().forEach(item ->
                    events.publishEvent(new ProductUnbookedEvent(item.getProductId(), userId, item.getQuantity())));
            cart.getItems().clear();
            cartRepo.save(cart);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItem> getCartItems(String userId) {
        return cartRepo.findByUserId(userId)
                .map(cart -> cart.getItems().stream().map(this::toItemDto).toList())
                .orElse(List.of());
    }

    // ─── Private helpers ──────────────────────────────────────────────────────

    private CartEntity getCartEntity(String userId) {
        return cartRepo.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user: " + userId));
    }

    private Cart toDto(CartEntity entity) {
        List<CartItem> items = entity.getItems().stream().map(this::toItemDto).toList();
        BigDecimal total = items.stream()
                .map(i -> i.unitPrice().multiply(BigDecimal.valueOf(i.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Cart(entity.getUserId(), items, total);
    }

    private CartItem toItemDto(CartItemEntity item) {
        return new CartItem(item.getProductId(), item.getProductName(), item.getQuantity(), item.getUnitPrice());
    }

    private void validateQuantity(int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
    }
}
