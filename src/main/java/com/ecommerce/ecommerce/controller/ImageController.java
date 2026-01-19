package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.ProductImageResponseDTO;
import com.ecommerce.ecommerce.dto.UploadImageRequestDTO;
import com.ecommerce.ecommerce.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/imagens")
@Tag(name = "Imagens", description = "Gerenciamento de imagens de produtos")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Upload de imagem para produto",
            description = "Faz upload de uma imagem para um produto específico"
    )
    @ApiResponse(responseCode = "201", description = "Imagem uploadada com sucesso")
    @ApiResponse(responseCode = "400", description = "Arquivo inválido ou produto não encontrado")
    public ResponseEntity<?> uploadProductImage(
            @Parameter(description = "Arquivo de imagem")
            @RequestParam("file") MultipartFile file,

            @Parameter(description = "Dados da imagem")
            @Valid @ModelAttribute UploadImageRequestDTO request) {

        try {
            ProductImageResponseDTO response = imageService.uploadProductImage(
                    request.getProdutoId(),
                    file,
                    request.isPrincipal()
            );

            Map<String, Object> result = new HashMap<>();
            result.put("message", "Imagem uploadada com sucesso");
            result.put("image", response);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao processar imagem: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping(value = "/upload-multiplas/{produtoId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Upload de múltiplas imagens",
            description = "Faz upload de várias imagens para um produto de uma vez"
    )
    public ResponseEntity<?> uploadMultipleImages(
            @Parameter(description = "ID do produto")
            @PathVariable Long produtoId,

            @Parameter(description = "Arquivos de imagem")
            @RequestParam("files") List<MultipartFile> files) {

        if (files == null || files.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Nenhum arquivo enviado"));
        }

        try {
            List<ProductImageResponseDTO> images = imageService.uploadMultipleProductImages(
                    produtoId, files);

            Map<String, Object> response = new HashMap<>();
            response.put("message", files.size() + " imagens uploadadas com sucesso");
            response.put("total", images.size());
            response.put("images", images);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao fazer upload: " + e.getMessage()));
        }
    }

    @GetMapping("/produto/{produtoId}")
    @Operation(
            summary = "Listar imagens do produto",
            description = "Retorna todas as imagens de um produto específico"
    )
    public ResponseEntity<?> getProductImages(
            @Parameter(description = "ID do produto")
            @PathVariable Long produtoId) {

        List<ProductImageResponseDTO> images = imageService.getProductImages(produtoId);

        Map<String, Object> response = new HashMap<>();
        response.put("produtoId", produtoId);
        response.put("total", images.size());
        response.put("images", images);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{imageId}")
    @Operation(summary = "Buscar imagem por ID")
    public ResponseEntity<?> getImage(
            @Parameter(description = "ID da imagem")
            @PathVariable Long imageId) {

        ProductImageResponseDTO image = imageService.getProductImage(imageId);
        return ResponseEntity.ok(image);
    }

    @PutMapping("/{imageId}/principal")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Definir imagem como principal",
            description = "Define uma imagem como a imagem principal do produto"
    )
    public ResponseEntity<?> setAsPrincipal(
            @Parameter(description = "ID da imagem")
            @PathVariable Long imageId) {

        ProductImageResponseDTO image = imageService.setAsPrincipal(imageId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Imagem definida como principal");
        response.put("image", image);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{imageId}/ordem")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Atualizar ordem da imagem",
            description = "Atualiza a posição/ordem de exibição da imagem"
    )
    public ResponseEntity<?> updateImageOrder(
            @Parameter(description = "ID da imagem")
            @PathVariable Long imageId,

            @Parameter(description = "Nova ordem (posição)")
            @RequestParam Integer ordem) {

        ProductImageResponseDTO image = imageService.updateImageOrder(imageId, ordem);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Ordem atualizada com sucesso");
        response.put("image", image);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{imageId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar imagem")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(
            @Parameter(description = "ID da imagem")
            @PathVariable Long imageId) {

        imageService.deleteProductImage(imageId);
    }

    @DeleteMapping("/produto/{produtoId}/todas")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Deletar todas as imagens do produto",
            description = "Remove todas as imagens associadas a um produto"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllProductImages(
            @Parameter(description = "ID do produto")
            @PathVariable Long produtoId) {

        imageService.deleteAllProductImages(produtoId);
    }

    @GetMapping("/config")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Obter configurações de upload",
            description = "Retorna as configurações atuais do sistema de upload"
    )
    public ResponseEntity<?> getUploadConfig() {
        Map<String, Object> config = imageService.getUploadConfig();
        return ResponseEntity.ok(config);
    }

    @GetMapping("/health")
    @Operation(
            summary = "Verificar saúde do sistema de upload",
            description = "Verifica se o sistema de upload está funcionando corretamente"
    )
    public ResponseEntity<?> checkUploadHealth() {
        try {
            imageService.validateUploadDirectory();

            Map<String, Object> health = new HashMap<>();
            health.put("status", "UP");
            health.put("uploadDir", imageService.getUploadConfig().get("uploadDirectory"));
            health.put("message", "Sistema de upload funcionando corretamente");

            return ResponseEntity.ok(health);

        } catch (IOException e) {
            Map<String, Object> health = new HashMap<>();
            health.put("status", "DOWN");
            health.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(health);
        }
    }
    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok(Map.of(
                "message", "pong",
                "service", "ImageService",
                "active", true,
                "timestamp", java.time.LocalDateTime.now().toString()
        ));
    }

    @PostMapping(value = "/test-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Testar upload (apenas desenvolvimento)",
            description = "Endpoint para testar upload sem precisar de produto"
    )
    public ResponseEntity<?> testUpload(
            @Parameter(description = "Arquivo para teste")
            @RequestParam("file") MultipartFile file,

            @Parameter(description = "Diretório de teste")
            @RequestParam(value = "directory", defaultValue = "test") String directory) {

        try {
            String url = imageService.uploadFile(file, directory);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Upload de teste realizado com sucesso");
            response.put("url", url);
            response.put("filename", file.getOriginalFilename());
            response.put("size", file.getSize());
            response.put("contentType", file.getContentType());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Erro no upload de teste: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}