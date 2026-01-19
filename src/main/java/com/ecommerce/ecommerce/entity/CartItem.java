package com.ecommerce.ecommerce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "carrinho_itens",
        uniqueConstraints = @UniqueConstraint(columnNames = {"carrinho_id", "produto_id"}))
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrinho_id", nullable = false)
    private Cart carrinho;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private Integer quantidade = 1;

    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    // Constructor
    public CartItem() {}

    public CartItem(Cart carrinho, Produto produto, Integer quantidade) {
        this.carrinho = carrinho;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = produto.getPrecoAtual();
    }

    // Business methods
    public BigDecimal getSubtotal() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    public void aumentarQuantidade(Integer quantidadeAdicional) {
        this.quantidade += quantidadeAdicional;
    }

    public void diminuirQuantidade(Integer quantidadeRemover) {
        this.quantidade = Math.max(0, this.quantidade - quantidadeRemover);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cart getCarrinho() { return carrinho; }
    public void setCarrinho(Cart carrinho) { this.carrinho = carrinho; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) {
        this.produto = produto;
        this.precoUnitario = produto.getPrecoAtual();
    }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }
}