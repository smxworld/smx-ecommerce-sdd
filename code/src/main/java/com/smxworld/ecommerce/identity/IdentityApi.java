package com.smxworld.ecommerce.identity;

/**
 * Public API of the Identity module.
 * Keycloak integration and JWT validation are handled internally.
 * Other modules receive {@code userId} as a plain parameter — no direct dependency on this module.
 */
public interface IdentityApi {
    // No public methods: identity validation happens transparently via Spring Security.
}
