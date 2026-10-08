package com.smxworld.ecommerce.review.internal.repository;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty("smx.modules.review.datasource.url")
class ReviewFlywayConfig {

    @Bean(initMethod = "migrate")
    Flyway reviewFlyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .defaultSchema("smx_review")
                .schemas("smx_review")
                .createSchemas(true)
                .locations("classpath:db/migration/smx_review")
                .baselineOnMigrate(true)
                .load();
    }
}
