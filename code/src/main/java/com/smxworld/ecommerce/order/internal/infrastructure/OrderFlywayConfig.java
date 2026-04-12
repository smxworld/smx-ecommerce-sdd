package com.smxworld.ecommerce.order.internal.infrastructure;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty("smx.modules.order.datasource.url")
class OrderFlywayConfig {

    @Bean(initMethod = "migrate")
    Flyway orderFlyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .defaultSchema("smx_order")
                .schemas("smx_order")
                .createSchemas(true)
                .locations("classpath:db/migration/smx_order")
                .baselineOnMigrate(true)
                .load();
    }
}
