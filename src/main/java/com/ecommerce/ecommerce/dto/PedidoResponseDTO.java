package com.ecommerce.ecommerce.dto;

import com.ecommerce.ecommerce.entity.EnderecoEntrega;
import com.ecommerce.ecommerce.entity.StatusPedido;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponseDTO {

    private Long id;
    private String codigoPedido;
    private Long usuarioId;
    private String usuarioNome;
    private String telefoneContato;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private StatusPedido status;
    private EnderecoEntrega enderecoEntrega;
    private BigDecimal valorTotal;
    private BigDecimal valorFrete;
    private BigDecimal desconto;
    private BigDecimal valorFinal;
    private String metodoPagamento;
    private String observacoes;
    private String linkWhatsapp;
    private List<ItemPedidoResponseDTO> itens;

    // Constructor
    public PedidoResponseDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigoPedido() { return codigoPedido; }
    public void setCodigoPedido(String codigoPedido) { this.codigoPedido = codigoPedido; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getUsuarioNome() { return usuarioNome; }
    public void setUsuarioNome(String usuarioNome) { this.usuarioNome = usuarioNome; }

    public String getTelefoneContato() { return telefoneContato; }
    public void setTelefoneContato(String telefoneContato) { this.telefoneContato = telefoneContato; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }

    public EnderecoEntrega getEnderecoEntrega() { return enderecoEntrega; }
    public void setEnderecoEntrega(EnderecoEntrega enderecoEntrega) { this.enderecoEntrega = enderecoEntrega; }

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

    public List<ItemPedidoResponseDTO> getItens() { return itens; }
    public void setItens(List<ItemPedidoResponseDTO> itens) { this.itens = itens; }
}