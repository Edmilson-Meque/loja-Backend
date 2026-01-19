package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.ProductImageResponseDTO;
import com.ecommerce.ecommerce.entity.ProductImage;
import com.ecommerce.ecommerce.entity.Produto;
import com.ecommerce.ecommerce.exception.BadRequestException;
import com.ecommerce.ecommerce.repository.ProductImageRepository;
import com.ecommerce.ecommerce.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

@Service
public class ImageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Value("${app.upload.base-url:http://localhost:8080/uploads}")
    private String baseUrl;

    @Value("${app.upload.allowed-extensions:jpg,jpeg,png,gif,webp}")
    private String[] allowedExtensions;

    @Value("${app.upload.max-size:5242880}") // 5MB default
    private long maxFileSize;

    @Value("${app.image.generate-thumbnails:true}")
    private boolean generateThumbnails;

    @Value("${app.image.thumbnail.width:150}")
    private int thumbnailWidth;

    @Value("${app.image.thumbnail.height:150}")
    private int thumbnailHeight;

    private final ProductImageRepository productImageRepository;
    private final ProdutoRepository produtoRepository;

    public ImageService(
            ProductImageRepository productImageRepository,
            ProdutoRepository produtoRepository
    ) {
        this.productImageRepository = productImageRepository;
        this.produtoRepository = produtoRepository;
    }

    // ============ VALIDAÇÃO DE ARQUIVOS ============

    public void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("O arquivo está vazio");
        }

        if (file.getSize() > maxFileSize) {
            throw new BadRequestException(
                    String.format("Arquivo muito grande. Tamanho máximo: %.2fMB",
                            maxFileSize / 1024.0 / 1024.0)
            );
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new BadRequestException("Nome do arquivo inválido");
        }

        String extension = getFileExtension(originalFilename).toLowerCase();
        if (!Arrays.asList(allowedExtensions).contains(extension)) {
            throw new BadRequestException(
                    String.format("Extensão de arquivo não permitida: %s. Permitidas: %s",
                            extension, String.join(", ", allowedExtensions))
            );
        }

        // Verifica se é realmente uma imagem
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new BadRequestException("Arquivo não é uma imagem válida");
            }
        } catch (IOException e) {
            throw new BadRequestException("Erro ao validar imagem: " + e.getMessage());
        }
    }

    // ============ UPLOAD BÁSICO ============

    public String uploadFile(MultipartFile file, String subdirectory) throws IOException {
        validateFile(file);

        // Cria diretório se não existir
        Path uploadPath = Paths.get(uploadDir, subdirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Gera nome único para o arquivo
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String uniqueFilename = generateUniqueFilename(extension);
        String finalFilename = uniqueFilename;

        // Salva o arquivo original
        Path filePath = uploadPath.resolve(finalFilename);
        Files.copy(file.getInputStream(), filePath);

        // Gera thumbnail se configurado
        if (generateThumbnails && isImageExtension(extension)) {
            try {
                generateThumbnail(file, uploadPath, uniqueFilename, extension);
            } catch (Exception e) {
                // Log do erro mas não falha o upload
                System.err.println("Erro ao gerar thumbnail: " + e.getMessage());
            }
        }

        // Retorna URL acessível
        return baseUrl + "/" + subdirectory + "/" + finalFilename;
    }

    // ============ UPLOAD PARA PRODUTOS ============

    @Transactional
    public ProductImageResponseDTO uploadProductImage(
            Long produtoId,
            MultipartFile file,
            boolean isPrincipal) throws IOException {

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new BadRequestException("Produto não encontrado"));

        // Faz upload do arquivo
        String imageUrl = uploadFile(file, "products/" + produtoId);

        // Se esta for a imagem principal, remove principal de outras
        if (isPrincipal) {
            productImageRepository.clearPrincipalFlag(produtoId);
        }

        // Cria entidade ProductImage
        ProductImage productImage = new ProductImage();
        productImage.setUrlImagem(imageUrl);
        productImage.setProduto(produto);
        productImage.setPrincipal(isPrincipal);

        // Define ordem (última posição)
        Integer maxOrdem = productImageRepository.findMaxOrdemByProdutoId(produtoId);
        productImage.setOrdem(maxOrdem != null ? maxOrdem + 1 : 1);

        // Salva no banco
        ProductImage savedImage = productImageRepository.save(productImage);

        // Se for a primeira imagem do produto, torna-a principal
        if (productImageRepository.countByProdutoId(produtoId) == 1) {
            productImageRepository.setAsPrincipal(savedImage.getId());
            savedImage.setPrincipal(true);
        }

        return convertToDTO(savedImage);
    }

    @Transactional
    public List<ProductImageResponseDTO> uploadMultipleProductImages(
            Long produtoId,
            List<MultipartFile> files) throws IOException {

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new BadRequestException("Produto não encontrado"));

        List<ProductImageResponseDTO> uploadedImages = new ArrayList<>();
        boolean isFirstUpload = productImageRepository.countByProdutoId(produtoId) == 0;

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            String imageUrl = uploadFile(file, "products/" + produtoId);

            ProductImage productImage = new ProductImage();
            productImage.setUrlImagem(imageUrl);
            productImage.setProduto(produto);
            productImage.setOrdem(i + 1);

            // Se for o primeiro upload e primeira imagem, define como principal
            productImage.setPrincipal(isFirstUpload && i == 0);

            ProductImage savedImage = productImageRepository.save(productImage);
            uploadedImages.add(convertToDTO(savedImage));
        }

        return uploadedImages;
    }

    // ============ GESTÃO DE IMAGENS ============

    @Transactional(readOnly = true)
    public List<ProductImageResponseDTO> getProductImages(Long produtoId) {
        List<ProductImage> images = productImageRepository.findByProdutoId(produtoId);
        return images.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductImageResponseDTO getProductImage(Long imageId) {
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new BadRequestException("Imagem não encontrada"));
        return convertToDTO(image);
    }

    @Transactional
    public ProductImageResponseDTO setAsPrincipal(Long imageId) {
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new BadRequestException("Imagem não encontrada"));

        // Remove principal de todas as imagens do produto
        productImageRepository.clearPrincipalFlag(image.getProduto().getId());

        // Define esta como principal
        productImageRepository.setAsPrincipal(imageId);
        image.setPrincipal(true);

        return convertToDTO(image);
    }

    @Transactional
    public ProductImageResponseDTO updateImageOrder(Long imageId, Integer novaOrdem) {
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new BadRequestException("Imagem não encontrada"));

        Long produtoId = image.getProduto().getId();
        List<ProductImage> images = productImageRepository.findByProdutoId(produtoId);

        // Valida a nova ordem
        if (novaOrdem < 1 || novaOrdem > images.size()) {
            throw new BadRequestException("Ordem inválida. Deve estar entre 1 e " + images.size());
        }

        // Reordena as imagens
        reorderImages(images, image.getId(), novaOrdem);

        // Salva todas as imagens com novas ordens
        productImageRepository.saveAll(images);

        return convertToDTO(image);
    }

    @Transactional
    public void deleteProductImage(Long imageId) {
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new BadRequestException("Imagem não encontrada"));

        boolean wasPrincipal = image.isPrincipal();
        Long produtoId = image.getProduto().getId();

        // Deleta arquivo físico
        deletePhysicalFile(image.getUrlImagem());

        // Deleta do banco
        productImageRepository.delete(image);

        // Se era imagem principal e ainda há imagens, define nova principal
        if (wasPrincipal) {
            List<ProductImage> remainingImages = productImageRepository.findByProdutoId(produtoId);
            if (!remainingImages.isEmpty()) {
                ProductImage newPrincipal = remainingImages.get(0);
                productImageRepository.setAsPrincipal(newPrincipal.getId());
            }
        }

        // Reordena as imagens restantes
        reorderAfterDeletion(produtoId);
    }

    @Transactional
    public void deleteAllProductImages(Long produtoId) {
        List<ProductImage> images = productImageRepository.findByProdutoId(produtoId);

        // Deleta arquivos físicos
        images.forEach(img -> deletePhysicalFile(img.getUrlImagem()));

        // Deleta do banco
        productImageRepository.deleteByProdutoId(produtoId);
    }

    // ============ MÉTODOS PRIVADOS AUXILIARES ============

    private void generateThumbnail(
            MultipartFile file,
            Path uploadPath,
            String baseFilename,
            String extension) throws IOException {

        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        if (originalImage == null) return;

        // Calcula dimensões mantendo proporção
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        double aspectRatio = (double) originalWidth / originalHeight;
        int newWidth, newHeight;

        if ((double) thumbnailWidth / thumbnailHeight > aspectRatio) {
            newWidth = (int) (thumbnailHeight * aspectRatio);
            newHeight = thumbnailHeight;
        } else {
            newWidth = thumbnailWidth;
            newHeight = (int) (thumbnailWidth / aspectRatio);
        }

        // Cria thumbnail
        BufferedImage thumbnail = new BufferedImage(newWidth, newHeight, originalImage.getType());
        Graphics2D g = thumbnail.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();

        // Salva thumbnail
        String thumbnailFilename = "thumb_" + baseFilename;
        Path thumbnailPath = uploadPath.resolve(thumbnailFilename);
        ImageIO.write(thumbnail, extension, thumbnailPath.toFile());
    }

    private void reorderImages(List<ProductImage> images, Long movedImageId, Integer newPosition) {
        // Encontra a imagem que está sendo movida
        ProductImage movedImage = null;
        for (ProductImage img : images) {
            if (img.getId().equals(movedImageId)) {
                movedImage = img;
                break;
            }
        }

        if (movedImage == null) return;

        // Remove a imagem da lista temporariamente
        images.remove(movedImage);

        // Insere na nova posição (índice base 0)
        images.add(newPosition - 1, movedImage);

        // Atualiza ordens
        for (int i = 0; i < images.size(); i++) {
            images.get(i).setOrdem(i + 1);
        }
    }

    private void reorderAfterDeletion(Long produtoId) {
        List<ProductImage> images = productImageRepository.findByProdutoId(produtoId);

        for (int i = 0; i < images.size(); i++) {
            images.get(i).setOrdem(i + 1);
        }

        productImageRepository.saveAll(images);
    }

    private void deletePhysicalFile(String imageUrl) {
        try {
            String relativePath = imageUrl.replace(baseUrl + "/", "");
            Path filePath = Paths.get(uploadDir, relativePath);

            // Deleta arquivo principal
            Files.deleteIfExists(filePath);

            // Deleta thumbnail se existir
            String fileName = filePath.getFileName().toString();
            String thumbFileName = "thumb_" + fileName;
            Path thumbPath = filePath.resolveSibling(thumbFileName);
            Files.deleteIfExists(thumbPath);

        } catch (IOException e) {
            System.err.println("Erro ao deletar arquivo físico: " + e.getMessage());
        }
    }

    private String generateUniqueFilename(String extension) {
        return UUID.randomUUID().toString() + "." + extension;
    }

    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);
    }

    private boolean isImageExtension(String extension) {
        String[] imageExtensions = {"jpg", "jpeg", "png", "gif", "webp", "bmp"};
        return Arrays.asList(imageExtensions).contains(extension.toLowerCase());
    }

    private ProductImageResponseDTO convertToDTO(ProductImage image) {
        ProductImageResponseDTO dto = new ProductImageResponseDTO();
        dto.setId(image.getId());
        dto.setProdutoId(image.getProduto().getId());
        dto.setUrlImagem(image.getUrlImagem());
        dto.setOrdem(image.getOrdem());
        dto.setPrincipal(image.isPrincipal());
        dto.setDataUpload(LocalDateTime.now());

        // Extrai nome do arquivo da URL
        if (image.getUrlImagem() != null) {
            String[] parts = image.getUrlImagem().split("/");
            dto.setNomeArquivo(parts[parts.length - 1]);
        }

        return dto;
    }

    // ============ MÉTODOS DE CONFIGURAÇÃO ============

    public Map<String, Object> getUploadConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("maxFileSizeMB", maxFileSize / 1024.0 / 1024.0);
        config.put("allowedExtensions", allowedExtensions);
        config.put("uploadDirectory", uploadDir);
        config.put("baseUrl", baseUrl);
        config.put("generateThumbnails", generateThumbnails);
        config.put("thumbnailWidth", thumbnailWidth);
        config.put("thumbnailHeight", thumbnailHeight);
        return config;
    }

    public void validateUploadDirectory() throws IOException {
        Path path = Paths.get(uploadDir);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
            System.out.println("Diretório de upload criado: " + path.toAbsolutePath());
        }

        // Cria subdiretórios necessários
        Path productsPath = Paths.get(uploadDir, "products");
        if (!Files.exists(productsPath)) {
            Files.createDirectories(productsPath);
        }
    }
}