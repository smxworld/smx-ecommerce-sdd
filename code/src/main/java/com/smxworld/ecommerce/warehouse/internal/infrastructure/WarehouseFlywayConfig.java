package com.smxworld.ecommerce.warehouse.internal.infrastructure;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty("smx.modules.warehouse.datasource.url")
class WarehouseFlywayConfig {

    @Bean(initMethod = "migrate")
    Flyway warehouseFlyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .defaultSchema("smx_warehouse")
                .schemas("smx_warehouse")
                .createSchemas(true)
                .locations("classpath:db/migration/smx_warehouse")
                .baselineOnMigrate(true)
                .load();
    }
}
