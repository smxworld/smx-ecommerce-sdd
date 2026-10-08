package com.smxworld.ecommerce.cart;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.Executor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.test.context.ActiveProfiles;

@ApplicationModuleTest
@ActiveProfiles("test")
class CartModuleTest {

    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        Executor taskExecutor() {
            return new SyncTaskExecutor();
        }
    }

    @Autowired
    CartApi cartApi;

    @PersistenceContext
    EntityManager em;

    @Test
    void clearCartRemovesItemsFromTheDatabase() {
        String userId = "buyer-" + UUID.randomUUID();
        cartApi.addItem(userId, UUID.randomUUID(), "Keyboard", new BigDecimal("49.90"), 2);
        cartApi.addItem(userId, UUID.randomUUID(), "Mouse", new BigDecimal("19.90"), 1);
        assertThat(cartApi.getCart(userId).items()).hasSize(2);

        cartApi.clearCart(userId);

        assertThat(cartApi.getCart(userId).items()).isEmpty();
        Long persisted = em.createQuery("select count(i) from CartItemEntity i where i.cart.userId = :u", Long.class)
                .setParameter("u", userId)
                .getSingleResult();
        assertThat(persisted).isZero();
    }
}
