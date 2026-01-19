package com.ecommerce.ecommerce.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.HtmlUtils;

@Configuration
public class XssConfig {

    @Bean
    public Module xssModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new JsonHtmlXssSerializer());
        module.addDeserializer(String.class, new JsonHtmlXssDeserializer());
        return module;
    }
}