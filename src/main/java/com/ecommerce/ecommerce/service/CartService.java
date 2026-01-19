package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.CartItemRequestDTO;
import com.ecommerce.ecommerce.dto.CartItemResponseDTO;
import com.ecommerce.ecommerce.dto.CartResponseDTO;
import com.ecommerce.ecommerce.entity.Cart;
import com.ecommerce.ecommerce.entity.CartItem;
import com.ecommerce.ecommerce.entity.Produto;
import com.ecommerce.ecommerce.entity.User;
import com.ecommerce.ecommerce.exception.BadRequestException;
import com.ecommerce.ecommerce.repository.CartItemRepository;
import com.ecommerce.ecommerce.repository.CartRepository;
import com.ecommerce.ecommerce.repository.ProdutoRepository;
import com.ecommerce.ecommerce.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProdutoRepository produtoRepository;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            UserRepository userRepository,
            ProdutoRepository produtoRepository
    ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.produtoRepository = produtoRepository;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Usuário não encontrado"));
    }

    private Cart getOrCreateCart(User usuario) {
        return cartRepository.findByUsuario(usuario)
                .orElseGet(() -> {
                    Cart novoCarrinho = new Cart(usuario);
                    return cartRepository.save(novoCarrinho);
                });
    }

    @Transactional
    public CartResponseDTO adicionarItem(CartItemRequestDTO dto) {
        User usuario = getCurrentUser();
        Cart carrinho = getOrCreateCart(usuario);

        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new BadRequestException("Produto não encontrado"));

        // Verifica estoque
        if (produto.getQuantidadeEstoque() < dto.getQuantidade()) {
            throw new BadRequestException("Estoque insuficiente. Disponível: " + produto.getQuantidadeEstoque());
        }

        // Verifica se item já existe no carrinho
        Optional<CartItem> itemExistente = cartItemRepository.findByCarrinhoIdAndProdutoId(
                carrinho.getId(), produto.getId());

        if (itemExistente.isPresent()) {
            // Atualiza quantidade do item existente
            CartItem item = itemExistente.get();
            Integer novaQuantidade = item.getQuantidade() + dto.getQuantidade();

            // Verifica estoque novamente
            if (produto.getQuantidadeEstoque() < novaQuantidade) {
                throw new BadRequestException("Estoque insuficiente para quantidade solicitada");
            }

            item.setQuantidade(novaQuantidade);
            cartItemRepository.save(item);
        } else {
            // Adiciona novo item
            CartItem novoItem = new CartItem(carrinho, produto, dto.getQuantidade());
            cartItemRepository.save(novoItem);
        }

        carrinho.atualizarData();
        cartRepository.save(carrinho);

        return buscarCarrinho();
    }

    @Transactional(readOnly = true)
    public CartResponseDTO buscarCarrinho() {
        User usuario = getCurrentUser();
        Cart carrinho = cartRepository.findByUsuarioIdWithItems(usuario.getId())
                .orElseGet(() -> new Cart(usuario));

        return convertToDTO(carrinho);
    }

    @Transactional
    public CartResponseDTO atualizarQuantidade(Long produtoId, Integer quantidade) {
        if (quantidade <= 0) {
            throw new BadRequestException("Quantidade deve ser maior que zero");
        }

        User usuario = getCurrentUser();
        Cart carrinho = getOrCreateCart(usuario);

        CartItem item = cartItemRepository.findByCarrinhoIdAndProdutoId(carrinho.getId(), produtoId)
                .orElseThrow(() -> new BadRequestException("Item não encontrado no carrinho"));

        Produto produto = item.getProduto();

        // Verifica estoque
        if (produto.getQuantidadeEstoque() < quantidade) {
            throw new BadRequestException("Estoque insuficiente. Disponível: " + produto.getQuantidadeEstoque());
        }

        item.setQuantidade(quantidade);
        cartItemRepository.save(item);

        carrinho.atualizarData();
        cartRepository.save(carrinho);

        return buscarCarrinho();
    }

    @Transactional
    public CartResponseDTO removerItem(Long produtoId) {
        User usuario = getCurrentUser();
        Cart carrinho = getOrCreateCart(usuario);

        cartItemRepository.deleteByCarrinhoIdAndProdutoId(carrinho.getId(), produtoId);

        carrinho.atualizarData();
        cartRepository.save(carrinho);

        return buscarCarrinho();
    }

    @Transactional
    public void limparCarrinho() {
        User usuario = getCurrentUser();
        Cart carrinho = getOrCreateCart(usuario);

        cartItemRepository.deleteByCarrinhoId(carrinho.getId());

        carrinho.atualizarData();
        cartRepository.save(carrinho);
    }

    @Transactional(readOnly = true)
    public Integer contarItens() {
        User usuario = getCurrentUser();
        Cart carrinho = cartRepository.findByUsuario(usuario)
                .orElseGet(() -> new Cart(usuario));

        return (int) cartItemRepository.countByCarrinhoId(carrinho.getId());
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularTotal() {
        User usuario = getCurrentUser();
        Cart carrinho = cartRepository.findByUsuarioIdWithItems(usuario.getId())
                .orElseGet(() -> new Cart(usuario));

        return carrinho.getTotal();
    }

    private CartResponseDTO convertToDTO(Cart carrinho) {
        CartResponseDTO dto = new CartResponseDTO();
        dto.setId(carrinho.getId());
        dto.setUsuarioId(carrinho.getUsuario().getId());
        dto.setDataAtualizacao(carrinho.getDataAtualizacao());
        dto.setTotal(carrinho.getTotal());
        dto.setQuantidadeTotal(carrinho.getQuantidadeTotal());
        dto.setQuantidadeItens(carrinho.getItens().size());

        List<CartItemResponseDTO> itensDTO = carrinho.getItens().stream()
                .map(this::convertItemToDTO)
                .toList();

        dto.setItens(itensDTO);
        return dto;
    }

    private CartItemResponseDTO convertItemToDTO(CartItem item) {
        CartItemResponseDTO dto = new CartItemResponseDTO();
        Produto produto = item.getProduto();

        dto.setId(item.getId());
        dto.setProdutoId(produto.getId());
        dto.setProdutoNome(produto.getNome());
        dto.setProdutoDescricaoCurta(produto.getShortDescription());
        dto.setProdutoPreco(produto.getPreco());
        dto.setProdutoPrecoPromocional(produto.getPrecoPromocional());
        dto.setProdutoEmPromocao(produto.getEmPromocao());
        dto.setQuantidade(item.getQuantidade());
        dto.setPrecoUnitario(item.getPrecoUnitario());
        dto.setSubtotal(item.getSubtotal());
        dto.setEstoqueDisponivel(produto.getQuantidadeEstoque());

        return dto;
    }
}