package com.smxworld.ecommerce.analytics.internal.infrastructure;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty("smx.modules.analytics.datasource.url")
class AnalyticsFlywayConfig {

    @Bean(initMethod = "migrate")
    Flyway analyticsFlyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .defaultSchema("smx_analytics")
                .schemas("smx_analytics")
                .createSchemas(true)
                .locations("classpath:db/migration/smx_analytics")
                .baselineOnMigrate(true)
                .load();
    }
}
