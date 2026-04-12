package com.smxworld.ecommerce.cart.internal.infrastructure;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty("smx.modules.cart.datasource.url")
class CartFlywayConfig {

    @Bean(initMethod = "migrate")
    Flyway cartFlyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .defaultSchema("smx_cart")
                .schemas("smx_cart")
                .createSchemas(true)
                .locations("classpath:db/migration/smx_cart")
                .baselineOnMigrate(true)
                .load();
    }
}
