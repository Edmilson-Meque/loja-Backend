package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.ProductImageResponseDTO;
import com.ecommerce.ecommerce.dto.ProdutoRequestDTO;
import com.ecommerce.ecommerce.dto.ProdutoResponseDTO;
import com.ecommerce.ecommerce.entity.Category;
import com.ecommerce.ecommerce.entity.Produto;
import com.ecommerce.ecommerce.exception.BadRequestException;
import com.ecommerce.ecommerce.repository.CategoryRepository;
import com.ecommerce.ecommerce.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoryRepository categoryRepository;
    private final FavoriteService favoriteService;
    private final ImageService imageService;

    public ProdutoService(
            ProdutoRepository produtoRepository,
            CategoryRepository categoryRepository,
            FavoriteService favoriteService,  ImageService imageService
    ) {
        this.produtoRepository = produtoRepository;
        this.categoryRepository = categoryRepository;
        this.favoriteService = favoriteService;
        this.imageService = imageService;
    }
    private boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null
                && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarTodos() {
        return produtoRepository.findAll().stream()
                .filter(produto -> !imageService.getProductImages(produto.getId()).isEmpty())
                .map(this::convertToDTO)
                .toList();
    }


    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Produto não encontrado"));

        return convertToDTO(produto);
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscarPorCategoria(Long categoriaId) {
        return produtoRepository.findAll().stream()
                .filter(produto -> produto.getCategoria() != null &&
                        produto.getCategoria().getId().equals(categoriaId))
                .filter(produto -> {
                    try {
                        return !imageService.getProductImages(produto.getId()).isEmpty();
                    } catch (Exception e) {
                        return false;
                    }
                })
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscarEmPromocao() {
        return produtoRepository.findAll().stream()
                .filter(Produto::isEmPromocao)
                .filter(produto -> {
                    try {
                        return !imageService.getProductImages(produto.getId()).isEmpty();
                    } catch (Exception e) {
                        return false;
                    }
                })
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscarOfertasDoDia() {
        return produtoRepository.findAll().stream()
                .filter(Produto::isOfertaDoDia)
                .filter(produto -> {
                    try {
                        return !imageService.getProductImages(produto.getId()).isEmpty();
                    } catch (Exception e) {
                        return false;
                    }
                })
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscarEmDestaque() {
        return produtoRepository.findAll().stream()
                .filter(Produto::isEmDestaque)
                .filter(produto -> {
                    try {
                        return !imageService.getProductImages(produto.getId()).isEmpty();
                    } catch (Exception e) {
                        return false;
                    }
                })
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional
    public ProdutoResponseDTO criar(ProdutoRequestDTO dto) {
        // Validação da categoria
        Category categoria = categoryRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new BadRequestException("Categoria não encontrada"));

        // Cria o produto
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setShortDescription(dto.getShortDescription());
        produto.setLongDescription(dto.getLongDescription());
        produto.setPreco(BigDecimal.valueOf(dto.getPreco()));
        produto.setQuantidadeEstoque(dto.getQuantidadeEstoque());
        produto.setCategoria(categoria);

        // Campos opcionais
        Optional.ofNullable(dto.getPrecoPromocional())
                .ifPresent(preco -> {
                    produto.setPrecoPromocional(BigDecimal.valueOf(preco));
                    produto.setEmPromocao(true);
                });

        Optional.ofNullable(dto.getSku()).ifPresent(produto::setSku);
        Optional.ofNullable(dto.isOfertaDoDia()).ifPresent(produto::setOfertaDoDia);
        Optional.ofNullable(dto.isEmDestaque()).ifPresent(produto::setEmDestaque);

        Produto salvo = produtoRepository.save(produto);
        return convertToDTO(salvo);
    }

    @Transactional
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Produto não encontrado"));

        // Atualiza categoria se fornecida
        if (dto.getCategoriaId() != null) {
            Category categoria = categoryRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new BadRequestException("Categoria não encontrada"));
            produto.setCategoria(categoria);
        }

        // Atualiza outros campos
        Optional.ofNullable(dto.getNome()).ifPresent(produto::setNome);
        Optional.ofNullable(dto.getShortDescription()).ifPresent(produto::setShortDescription);
        Optional.ofNullable(dto.getLongDescription()).ifPresent(produto::setLongDescription);
        Optional.ofNullable(dto.getPreco()).ifPresent(preco ->
                produto.setPreco(BigDecimal.valueOf(preco)));
        Optional.ofNullable(dto.getQuantidadeEstoque()).ifPresent(produto::setQuantidadeEstoque);

        // Campos de promoção
        if (dto.getPrecoPromocional() != null) {
            produto.setPrecoPromocional(BigDecimal.valueOf(dto.getPrecoPromocional()));
            produto.setEmPromocao(true);
        } else {
            produto.setEmPromocao(false);
        }

        Optional.ofNullable(dto.getSku()).ifPresent(produto::setSku);
        Optional.ofNullable(dto.isOfertaDoDia()).ifPresent(produto::setOfertaDoDia);
        Optional.ofNullable(dto.isEmDestaque()).ifPresent(produto::setEmDestaque);

        Produto atualizado = produtoRepository.save(produto);
        return convertToDTO(atualizado);
    }

    @Transactional
    public void deletar(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new BadRequestException("Produto não encontrado");
        }
        produtoRepository.deleteById(id);
    }

    private ProdutoResponseDTO convertToDTO(Produto produto) {
        ProdutoResponseDTO dto = new ProdutoResponseDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setShortDescription(produto.getShortDescription());
        dto.setLongDescription(produto.getLongDescription());
        dto.setPreco(produto.getPreco().doubleValue());
        dto.setQuantidadeEstoque(produto.getQuantidadeEstoque());

        // Campos de promoção
        dto.setPrecoPromocional(
                produto.getPrecoPromocional() != null ?
                        produto.getPrecoPromocional().doubleValue() : null
        );
        dto.setEmPromocao(Boolean.TRUE.equals(produto.getEmPromocao()));
        dto.setOfertaDoDia(Boolean.TRUE.equals(produto.getOfertaDoDia()));
        dto.setEmDestaque(Boolean.TRUE.equals(produto.getEmDestaque()));

        dto.setSku(produto.getSku());

        if (produto.getCategoria() != null) {
            dto.setCategoriaId(produto.getCategoria().getId());
            dto.setCategoriaNome(produto.getCategoria().getNome());
        }

        // Calcula preço atual
        dto.setPrecoAtual(produto.getPrecoAtual().doubleValue());
        dto.setPercentualDesconto(produto.getPercentualDesconto());

        if (isAuthenticated()) {
            dto.setFavoritado(favoriteService.isFavorito(produto.getId()));
        } else {
            dto.setFavoritado(false); // público sempre false (ou null, se quiser)
        }
        // Tentar popular a imagem principal e total de imagens
        try {
            List<ProductImageResponseDTO> imagens = imageService.getProductImages(produto.getId());
            if (imagens != null && !imagens.isEmpty()) {
                var principal = imagens.stream().filter(ProductImageResponseDTO::isPrincipal).findFirst();
                if (principal.isPresent()) {
                    dto.setImagemPrincipal(principal.get().getUrlImagem());
                } else {
                    dto.setImagemPrincipal(imagens.get(0).getUrlImagem());
                }
                dto.setTotalImagens(imagens.size());
                dto.setImagensUrls(imagens.stream().map(ProductImageResponseDTO::getUrlImagem).toList());
            }
        } catch (Exception e) {
            // Ignorar erros ao buscar imagens
        }

        return dto;
    }
    public ProdutoResponseDTO buscarPorIdComImagens(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Produto não encontrado"));

        ProdutoResponseDTO dto = convertToDTO(produto);

        // Buscar imagens do produto
        try {
            List<ProductImageResponseDTO> imagens = imageService.getProductImages(id);
            // Aqui você pode adicionar as imagens ao DTO
            // Você precisará atualizar o ProdutoResponseDTO para incluir imagens
        } catch (Exception e) {
            // Log do erro, mas não falha a operação
            System.err.println("Erro ao buscar imagens do produto: " + e.getMessage());
        }
        try {
            List<ProductImageResponseDTO> imagens = imageService.getProductImages(produto.getId());
            if (!imagens.isEmpty()) {
                // Encontrar imagem principal
                Optional<ProductImageResponseDTO> imagemPrincipal = imagens.stream()
                        .filter(ProductImageResponseDTO::isPrincipal)
                        .findFirst();

                if (imagemPrincipal.isPresent()) {
                    dto.setImagemPrincipal(imagemPrincipal.get().getUrlImagem());
                } else {
                    // Usar primeira imagem se não houver principal
                    dto.setImagemPrincipal(imagens.get(0).getUrlImagem());
                }

                dto.setTotalImagens(imagens.size());
            }
        } catch (Exception e) {
            // Silenciosamente ignorar erro
        }

        return dto;
    }

}
