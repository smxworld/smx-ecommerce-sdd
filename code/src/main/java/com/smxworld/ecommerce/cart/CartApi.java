package com.smxworld.ecommerce.cart;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Public API of the Cart module.
 */
public interface CartApi {

    Cart getCart(String userId);

    Cart addItem(String userId, UUID productId, String productName, BigDecimal unitPrice, int quantity);

    Cart updateItem(String userId, UUID productId, int quantity);

    Cart removeItem(String userId, UUID productId);

    void clearCart(String userId);

    /** Called by the Order module at checkout to retrieve the cart contents. */
    List<CartItem> getCartItems(String userId);
}
