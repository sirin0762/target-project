package com.example.targetproject.logger;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class SqlTimingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TimingStatementInspector timingStatementInspector() {
        return new TimingStatementInspector();
    }
}