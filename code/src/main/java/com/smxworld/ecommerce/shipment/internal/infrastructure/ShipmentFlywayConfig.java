package com.smxworld.ecommerce.shipment.internal.infrastructure;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty("smx.modules.shipment.datasource.url")
class ShipmentFlywayConfig {

    @Bean(initMethod = "migrate")
    Flyway shipmentFlyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .defaultSchema("smx_shipment")
                .schemas("smx_shipment")
                .createSchemas(true)
                .locations("classpath:db/migration/smx_shipment")
                .baselineOnMigrate(true)
                .load();
    }
}
