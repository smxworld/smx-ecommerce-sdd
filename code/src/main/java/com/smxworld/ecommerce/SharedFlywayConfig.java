package com.smxworld.ecommerce;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Esegue le migration Flyway per lo schema public (condiviso).
 * Crea la tabella event_publication richiesta da Spring Modulith
 * prima che Hibernate validi lo schema con ddl-auto=validate.
 */
@Configuration
class SharedFlywayConfig {

    @Bean(initMethod = "migrate")
    Flyway sharedFlyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .defaultSchema("public")
                .schemas("public")
                .locations("classpath:db/migration/public")
                .baselineOnMigrate(true)
                .load();
    }
}
