package com.ecommerce.ecommerce.config;

import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.hibernate.engine.jdbc.internal.Formatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SqlInjectionConfig {

    @Bean
    public Formatter sqlFormatter() {
        return FormatStyle.BASIC.getFormatter();
    }
}