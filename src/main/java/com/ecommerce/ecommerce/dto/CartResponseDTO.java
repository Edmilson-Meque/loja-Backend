package com.ecommerce.ecommerce.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CartResponseDTO {

    private Long id;
    private Long usuarioId;
    private LocalDateTime dataAtualizacao;
    private List<CartItemResponseDTO> itens;
    private BigDecimal total;
    private Integer quantidadeTotal;
    private Integer quantidadeItens;

    // Constructor
    public CartResponseDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public List<CartItemResponseDTO> getItens() { return itens; }
    public void setItens(List<CartItemResponseDTO> itens) { this.itens = itens; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public Integer getQuantidadeTotal() { return quantidadeTotal; }
    public void setQuantidadeTotal(Integer quantidadeTotal) { this.quantidadeTotal = quantidadeTotal; }

    public Integer getQuantidadeItens() { return quantidadeItens; }
    public void setQuantidadeItens(Integer quantidadeItens) { this.quantidadeItens = quantidadeItens; }
}