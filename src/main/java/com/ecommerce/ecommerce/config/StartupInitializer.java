package com.ecommerce.ecommerce.config;

import com.ecommerce.ecommerce.service.ImageService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StartupInitializer {

    private final ImageService imageService;

    public StartupInitializer(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostConstruct
    public void init() {
        try {
            // Cria diretórios de upload
            imageService.validateUploadDirectory();
            System.out.println("✅ Diretórios de upload inicializados com sucesso!");

        } catch (IOException e) {
            System.err.println("❌ Erro ao inicializar diretórios de upload: " + e.getMessage());
        }
    }
}