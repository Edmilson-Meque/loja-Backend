package com.ecommerce.ecommerce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_pedido", unique = true, nullable = false)
    private String codigoPedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status = StatusPedido.AGUARDANDO_PAGAMENTO;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    @Embedded
    private EnderecoEntrega enderecoEntrega;

    @Column(name = "telefone_contato", nullable = false)
    private String telefoneContato;

    @Column(name = "valor_total", precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "valor_frete", precision = 10, scale = 2)
    private BigDecimal valorFrete = BigDecimal.ZERO;

    @Column(name = "desconto", precision = 10, scale = 2)
    private BigDecimal desconto = BigDecimal.ZERO;

    @Column(name = "valor_final", precision = 10, scale = 2)
    private BigDecimal valorFinal;

    @Column(name = "metodo_pagamento")
    private String metodoPagamento = "WhatsApp";

    @Column(name = "observacoes", length = 1000)
    private String observacoes;

    @Column(name = "link_whatsapp")
    private String linkWhatsapp;

    // Constructor
    public Pedido() {
        this.codigoPedido = "PED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    public Pedido(User usuario) {
        this();
        this.usuario = usuario;
        this.telefoneContato = usuario.getEmail(); // Inicial com email
    }

    // Business methods
    public void calcularTotais() {
        BigDecimal totalItens = itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.valorTotal = totalItens;
        this.valorFinal = totalItens
                .add(valorFrete)
                .subtract(desconto);
    }

    public void atualizarStatus(StatusPedido novoStatus) {
        this.status = novoStatus;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public boolean podeSerCancelado() {
        return status == StatusPedido.AGUARDANDO_PAGAMENTO ||
                status == StatusPedido.PROCESSANDO;
    }

    public void gerarLinkWhatsapp(String numeroWhatsappEmpresa) {
        // Formatar mensagem para WhatsApp
        String mensagem = gerarMensagemWhatsapp();

        // Codificar mensagem para URL
        String mensagemCodificada = java.net.URLEncoder.encode(mensagem, java.nio.charset.StandardCharsets.UTF_8);

        // Gerar link do WhatsApp
        this.linkWhatsapp = String.format("https://wa.me/%s?text=%s",
                numeroWhatsappEmpresa,
                mensagemCodificada);
    }

    public String gerarMensagemWhatsapp() {
        StringBuilder mensagem = new StringBuilder();
        mensagem.append("🛒 *NOVO PEDIDO - ").append(codigoPedido).append("*\n\n");
        mensagem.append("*Cliente:* ").append(usuario.getNome()).append("\n");
        mensagem.append("*Email:* ").append(usuario.getEmail()).append("\n");
        mensagem.append("*Telefone:* ").append(telefoneContato).append("\n\n");

        mensagem.append("*📦 ITENS DO PEDIDO:*\n");
        for (ItemPedido item : itens) {
            mensagem.append("• ").append(item.getNomeProduto())
                    .append(" - Qtd: ").append(item.getQuantidade())
                    .append(" - R$ ").append(item.getPrecoUnitario())
                    .append(" cada\n");
        }

        mensagem.append("\n*💰 VALORES:*\n");
        mensagem.append("Subtotal: R$ ").append(valorTotal).append("\n");
        if (valorFrete.compareTo(BigDecimal.ZERO) > 0) {
            mensagem.append("Frete: R$ ").append(valorFrete).append("\n");
        }
        if (desconto.compareTo(BigDecimal.ZERO) > 0) {
            mensagem.append("Desconto: R$ ").append(desconto).append("\n");
        }
        mensagem.append("*TOTAL: R$ ").append(valorFinal).append("*\n\n");

        if (enderecoEntrega != null) {
            mensagem.append("*📍 ENDEREÇO DE ENTREGA:*\n");
            mensagem.append(enderecoEntrega.getLogradouro()).append(", ")
                    .append(enderecoEntrega.getNumero()).append("\n");
            if (enderecoEntrega.getComplemento() != null && !enderecoEntrega.getComplemento().isEmpty()) {
                mensagem.append("Complemento: ").append(enderecoEntrega.getComplemento()).append("\n");
            }
            mensagem.append(enderecoEntrega.getBairro()).append("\n");
            mensagem.append(enderecoEntrega.getCidade()).append(" - ")
                    .append(enderecoEntrega.getEstado()).append("\n");
            mensagem.append("CEP: ").append(enderecoEntrega.getCep()).append("\n\n");
        }

        if (observacoes != null && !observacoes.isEmpty()) {
            mensagem.append("*📝 OBSERVAÇÕES:*\n").append(observacoes).append("\n\n");
        }

        mensagem.append("📱 *Status do Pedido:* ").append(status.getDescricao()).append("\n");
        mensagem.append("🕐 *Data:* ").append(dataCriacao.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        return mensagem.toString();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigoPedido() { return codigoPedido; }
    public void setCodigoPedido(String codigoPedido) { this.codigoPedido = codigoPedido; }

    public User getUsuario() { return usuario; }
    public void setUsuario(User usuario) { this.usuario = usuario; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }

    public List<ItemPedido> getItens() { return itens; }
    public void setItens(List<ItemPedido> itens) { this.itens = itens; }

    public EnderecoEntrega getEnderecoEntrega() { return enderecoEntrega; }
    public void setEnderecoEntrega(EnderecoEntrega enderecoEntrega) { this.enderecoEntrega = enderecoEntrega; }

    public String getTelefoneContato() { return telefoneContato; }
    public void setTelefoneContato(String telefoneContato) { this.telefoneContato = telefoneContato; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public BigDecimal getValorFrete() { return valorFrete; }
    public void setValorFrete(BigDecimal valorFrete) { this.valorFrete = valorFrete; }

    public BigDecimal getDesconto() { return desconto; }
    public void setDesconto(BigDecimal desconto) { this.desconto = desconto; }

    public BigDecimal getValorFinal() { return valorFinal; }
    public void setValorFinal(BigDecimal valorFinal) { this.valorFinal = valorFinal; }

    public String getMetodoPagamento() { return metodoPagamento; }
    public void setMetodoPagamento(String metodoPagamento) { this.metodoPagamento = metodoPagamento; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public String getLinkWhatsapp() { return linkWhatsapp; }
    public void setLinkWhatsapp(String linkWhatsapp) { this.linkWhatsapp = linkWhatsapp; }
}