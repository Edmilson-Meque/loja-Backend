package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.*;
import com.ecommerce.ecommerce.entity.*;
import com.ecommerce.ecommerce.exception.BadRequestException;
import com.ecommerce.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProdutoRepository produtoRepository;

    @Value("${app.whatsapp.numero:5511999999999}")
    private String numeroWhatsappEmpresa;

    @Value("${app.whatsapp.habilitado:true}")
    private boolean whatsappHabilitado;

    public PedidoService(
            PedidoRepository pedidoRepository,
            ItemPedidoRepository itemPedidoRepository,
            UserRepository userRepository,
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProdutoRepository produtoRepository
    ) {
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.produtoRepository = produtoRepository;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Usuário não encontrado"));
    }

    @Transactional
    public PedidoResponseDTO criarPedido(CriarPedidoRequestDTO dto) {
        User usuario = getCurrentUser();

        // Buscar carrinho do usuário
        Cart carrinho = cartRepository.findByUsuarioIdWithItems(usuario.getId())
                .orElseThrow(() -> new BadRequestException("Carrinho vazio"));

        if (carrinho.getItens().isEmpty()) {
            throw new BadRequestException("Carrinho vazio");
        }

        // Verificar estoque
        for (CartItem item : carrinho.getItens()) {
            Produto produto = item.getProduto();
            if (produto.getQuantidadeEstoque() < item.getQuantidade()) {
                throw new BadRequestException(
                        "Estoque insuficiente para o produto: " + produto.getNome() +
                                ". Disponível: " + produto.getQuantidadeEstoque()
                );
            }
        }

        // Criar pedido
        Pedido pedido = new Pedido(usuario);
        pedido.setEnderecoEntrega(convertEnderecoDTO(dto.getEnderecoEntrega()));
        pedido.setTelefoneContato(dto.getTelefoneContato());
        pedido.setObservacoes(dto.getObservacoes());
        pedido.setValorFrete(dto.getValorFrete());
        pedido.setDesconto(dto.getDesconto());

        // Adicionar itens do carrinho ao pedido
        for (CartItem cartItem : carrinho.getItens()) {
            ItemPedido itemPedido = new ItemPedido(pedido, cartItem.getProduto(), cartItem.getQuantidade());
            pedido.getItens().add(itemPedido);

            // Atualizar estoque
            Produto produto = cartItem.getProduto();
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - cartItem.getQuantidade());
            produtoRepository.save(produto);
        }

        // Calcular totais
        pedido.calcularTotais();

        // Gerar link do WhatsApp
        if (whatsappHabilitado && numeroWhatsappEmpresa != null && !numeroWhatsappEmpresa.trim().isEmpty()) {
            pedido.gerarLinkWhatsapp(numeroWhatsappEmpresa);
        }

        pedido = pedidoRepository.save(pedido);

        // Limpar carrinho
        cartItemRepository.deleteByCarrinhoId(carrinho.getId());
        carrinho.atualizarData();
        cartRepository.save(carrinho);

        return convertToDTO(pedido);
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO buscarPedido(Long pedidoId) {
        User usuario = getCurrentUser();
        Pedido pedido = pedidoRepository.findByUsuarioAndId(usuario, pedidoId)
                .orElseThrow(() -> new BadRequestException("Pedido não encontrado"));
        return convertToDTO(pedido);
    }

    @Transactional
    public PedidoResponseDTO reenviarWhatsapp(Long pedidoId) {
        User usuario = getCurrentUser();
        Pedido pedido = pedidoRepository.findByUsuarioAndId(usuario, pedidoId)
                .orElseThrow(() -> new BadRequestException("Pedido não encontrado"));

        if (!whatsappHabilitado || numeroWhatsappEmpresa == null || numeroWhatsappEmpresa.trim().isEmpty()) {
            throw new BadRequestException("Integração com WhatsApp não configurada");
        }

        // Regenerar link do WhatsApp
        pedido.gerarLinkWhatsapp(numeroWhatsappEmpresa);
        pedido = pedidoRepository.save(pedido);

        return convertToDTO(pedido);
    }

    @Transactional
    public PedidoResponseDTO cancelarPedido(Long pedidoId) {
        User usuario = getCurrentUser();
        Pedido pedido = pedidoRepository.findByUsuarioAndId(usuario, pedidoId)
                .orElseThrow(() -> new BadRequestException("Pedido não encontrado"));

        if (!pedido.podeSerCancelado()) {
            throw new BadRequestException("Pedido não pode ser cancelado no status atual");
        }

        // Devolver itens ao estoque
        for (ItemPedido item : pedido.getItens()) {
            Produto produto = item.getProduto();
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + item.getQuantidade());
            produtoRepository.save(produto);
        }

        pedido.atualizarStatus(StatusPedido.CANCELADO);
        pedido = pedidoRepository.save(pedido);

        return convertToDTO(pedido);
    }

    // Métodos ADMIN
    @Transactional
    public PedidoResponseDTO atualizarStatusPedido(Long pedidoId, StatusPedido novoStatus) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new BadRequestException("Pedido não encontrado"));

        pedido.atualizarStatus(novoStatus);
        pedido = pedidoRepository.save(pedido);

        return convertToDTO(pedido);
    }

    private EnderecoEntrega convertEnderecoDTO(EnderecoEntregaDTO dto) {
        if (dto == null) return null;

        EnderecoEntrega endereco = new EnderecoEntrega();
        endereco.setCep(dto.getCep());
        endereco.setLogradouro(dto.getLogradouro());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setBairro(dto.getBairro());
        endereco.setCidade(dto.getCidade());
        endereco.setEstado(dto.getEstado());
        endereco.setReferencia(dto.getReferencia());

        return endereco;
    }

    private PedidoResponseDTO convertToDTO(Pedido pedido) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(pedido.getId());
        dto.setCodigoPedido(pedido.getCodigoPedido());
        dto.setUsuarioId(pedido.getUsuario().getId());
        dto.setUsuarioNome(pedido.getUsuario().getNome());
        dto.setTelefoneContato(pedido.getTelefoneContato());
        dto.setDataCriacao(pedido.getDataCriacao());
        dto.setDataAtualizacao(pedido.getDataAtualizacao());
        dto.setStatus(pedido.getStatus());
        dto.setEnderecoEntrega(pedido.getEnderecoEntrega());
        dto.setValorTotal(pedido.getValorTotal());
        dto.setValorFrete(pedido.getValorFrete());
        dto.setDesconto(pedido.getDesconto());
        dto.setValorFinal(pedido.getValorFinal());
        dto.setMetodoPagamento(pedido.getMetodoPagamento());
        dto.setObservacoes(pedido.getObservacoes());
        dto.setLinkWhatsapp(pedido.getLinkWhatsapp());

        dto.setItens(pedido.getItens().stream()
                .map(this::convertItemToDTO)
                .collect(Collectors.toList()));

        return dto;
    }

    private ItemPedidoResponseDTO convertItemToDTO(ItemPedido item) {
        ItemPedidoResponseDTO dto = new ItemPedidoResponseDTO();
        dto.setId(item.getId());
        dto.setProdutoId(item.getProduto().getId());
        dto.setProdutoNome(item.getNomeProduto());
        dto.setQuantidade(item.getQuantidade());
        dto.setPrecoUnitario(item.getPrecoUnitario());
        dto.setSubtotal(item.getSubtotal());
        return dto;
    }
}