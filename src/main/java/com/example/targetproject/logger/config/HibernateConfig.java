package com.example.targetproject.logger.config;

import com.example.targetproject.logger.JpaQueryInspector;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfig {

    private final JpaQueryInspector jpaQueryInspector;

    public HibernateConfig(JpaQueryInspector jpaQueryInspector) {
        this.jpaQueryInspector = jpaQueryInspector;
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return hibernateProperties ->
            hibernateProperties.put(AvailableSettings.STATEMENT_INSPECTOR, jpaQueryInspector);
    }

}
