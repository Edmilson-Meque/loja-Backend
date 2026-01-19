package com.ecommerce.ecommerce.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

@Configuration
public class MultipartConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();

        // Tamanho máximo do arquivo individual: 5MB
        factory.setMaxFileSize(DataSize.ofMegabytes(5));

        // Tamanho máximo total da requisição: 20MB
        factory.setMaxRequestSize(DataSize.ofMegabytes(20));

        // Tamanho limite após o qual os arquivos serão gravados em disco
        factory.setFileSizeThreshold(DataSize.ofKilobytes(512));

        return factory.createMultipartConfig();
    }
}