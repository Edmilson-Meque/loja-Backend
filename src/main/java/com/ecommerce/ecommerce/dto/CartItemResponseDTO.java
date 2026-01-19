package com.ecommerce.ecommerce.dto;

import java.math.BigDecimal;

public class CartItemResponseDTO {

    private Long id;
    private Long produtoId;
    private String produtoNome;
    private String produtoDescricaoCurta;
    private BigDecimal produtoPreco;
    private BigDecimal produtoPrecoPromocional;
    private Boolean produtoEmPromocao;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;
    private Integer estoqueDisponivel;

    // Constructor
    public CartItemResponseDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

    public String getProdutoNome() { return produtoNome; }
    public void setProdutoNome(String produtoNome) { this.produtoNome = produtoNome; }

    public String getProdutoDescricaoCurta() { return produtoDescricaoCurta; }
    public void setProdutoDescricaoCurta(String produtoDescricaoCurta) { this.produtoDescricaoCurta = produtoDescricaoCurta; }

    public BigDecimal getProdutoPreco() { return produtoPreco; }
    public void setProdutoPreco(BigDecimal produtoPreco) { this.produtoPreco = produtoPreco; }

    public BigDecimal getProdutoPrecoPromocional() { return produtoPrecoPromocional; }
    public void setProdutoPrecoPromocional(BigDecimal produtoPrecoPromocional) { this.produtoPrecoPromocional = produtoPrecoPromocional; }

    public Boolean getProdutoEmPromocao() { return produtoEmPromocao; }
    public void setProdutoEmPromocao(Boolean produtoEmPromocao) { this.produtoEmPromocao = produtoEmPromocao; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public Integer getEstoqueDisponivel() { return estoqueDisponivel; }
    public void setEstoqueDisponivel(Integer estoqueDisponivel) { this.estoqueDisponivel = estoqueDisponivel; }
}