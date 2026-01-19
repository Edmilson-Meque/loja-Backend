package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.FavoriteResponseDTO;
import com.ecommerce.ecommerce.entity.Favorite;
import com.ecommerce.ecommerce.entity.Produto;
import com.ecommerce.ecommerce.entity.User;
import com.ecommerce.ecommerce.exception.BadRequestException;
import com.ecommerce.ecommerce.repository.FavoriteRepository;
import com.ecommerce.ecommerce.repository.ProdutoRepository;
import com.ecommerce.ecommerce.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ProdutoRepository produtoRepository;

    public FavoriteService(
            FavoriteRepository favoriteRepository,
            UserRepository userRepository,
            ProdutoRepository produtoRepository
    ) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.produtoRepository = produtoRepository;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Usuário não encontrado"));
    }

    @Transactional
    public FavoriteResponseDTO adicionarFavorito(Long produtoId) {
        User usuario = getCurrentUser();
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new BadRequestException("Produto não encontrado"));

        // Verificar se já é favorito
        if (favoriteRepository.existsByUsuarioAndProduto(usuario, produto)) {
            throw new BadRequestException("Produto já está nos favoritos");
        }

        Favorite favorito = new Favorite(usuario, produto);
        Favorite salvo = favoriteRepository.save(favorito);

        return convertToDTO(salvo);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponseDTO> listarFavoritos() {
        User usuario = getCurrentUser();
        return favoriteRepository.findByUsuarioId(usuario.getId()).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional
    public void removerFavorito(Long produtoId) {
        User usuario = getCurrentUser();
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new BadRequestException("Produto não encontrado"));

        favoriteRepository.deleteByUsuarioAndProduto(usuario, produto);
    }

    @Transactional(readOnly = true)
    public boolean isFavorito(Long produtoId) {
        User usuario = getCurrentUser();
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new BadRequestException("Produto não encontrado"));

        return favoriteRepository.existsByUsuarioAndProduto(usuario, produto);
    }

    @Transactional(readOnly = true)
    public boolean isFavoritoByUsuarioId(Long usuarioId, Long produtoId) {
        return favoriteRepository.existsByUsuarioIdAndProdutoId(usuarioId, produtoId);
    }

    @Transactional(readOnly = true)
    public long contarFavoritos() {
        User usuario = getCurrentUser();
        return favoriteRepository.countByUsuarioId(usuario.getId());
    }

    private FavoriteResponseDTO convertToDTO(Favorite favorito) {
        FavoriteResponseDTO dto = new FavoriteResponseDTO();
        Produto produto = favorito.getProduto();

        dto.setId(favorito.getId());
        dto.setProdutoId(produto.getId());
        dto.setProdutoNome(produto.getNome());
        dto.setProdutoDescricaoCurta(produto.getShortDescription());
        dto.setProdutoPreco(produto.getPreco());
        dto.setProdutoPrecoPromocional(produto.getPrecoPromocional());
        dto.setProdutoEmPromocao(produto.getEmPromocao());
        dto.setDataAdicao(favorito.getDataAdicao());

        // Calcula preço atual
        BigDecimal precoAtual = produto.getPrecoAtual();
        dto.setProdutoPrecoAtual(precoAtual);

        // Percentual de desconto
        if (Boolean.TRUE.equals(produto.getEmPromocao()) && produto.getPrecoPromocional() != null) {
            BigDecimal desconto = produto.getPreco().subtract(produto.getPrecoPromocional());
            BigDecimal percentual = desconto.divide(produto.getPreco(), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            dto.setPercentualDesconto(percentual.intValue());
        } else {
            dto.setPercentualDesconto(0);
        }

        return dto;
    }
}