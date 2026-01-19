package com.ecommerce.ecommerce.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
public class SecurityHeadersConfig {

    @Bean
    public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
        FilterRegistrationBean<ForwardedHeaderFilter> filterRegBean =
                new FilterRegistrationBean<>();
        filterRegBean.setFilter(new ForwardedHeaderFilter());

        // Ordem HIGHEST_PRECEDENCE para garantir que seja executado primeiro
        filterRegBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);

        return filterRegBean;
    }

    @Bean
    public SecurityHeadersFilter securityHeadersFilter() {
        return new SecurityHeadersFilter();
    }
}