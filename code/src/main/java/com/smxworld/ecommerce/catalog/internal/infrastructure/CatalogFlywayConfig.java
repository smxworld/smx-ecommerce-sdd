package com.smxworld.ecommerce.catalog.internal.infrastructure;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Configura Flyway per il modulo catalog (schema smx_catalog).
 * Attivo solo quando il datasource PostgreSQL è configurato (non nei test con H2).
 */
@Configuration
@ConditionalOnProperty("smx.modules.catalog.datasource.url")
class CatalogFlywayConfig {

    @Bean(initMethod = "migrate")
    Flyway catalogFlyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .defaultSchema("smx_catalog")
                .schemas("smx_catalog")
                .createSchemas(true)
                .locations("classpath:db/migration/smx_catalog")
                .baselineOnMigrate(true)
                .load();
    }
}
