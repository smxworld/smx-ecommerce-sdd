package com.smxworld.ecommerce.payment.internal.infrastructure;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty("smx.modules.payment.datasource.url")
class PaymentFlywayConfig {

    @Bean(initMethod = "migrate")
    Flyway paymentFlyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .defaultSchema("smx_payment")
                .schemas("smx_payment")
                .createSchemas(true)
                .locations("classpath:db/migration/smx_payment")
                .baselineOnMigrate(true)
                .load();
    }
}
