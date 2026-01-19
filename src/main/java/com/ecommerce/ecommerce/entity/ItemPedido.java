package com.ecommerce.ecommerce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "pedido_itens")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "nome_produto")
    private String nomeProduto;

    // Constructor
    public ItemPedido() {}

    public ItemPedido(Pedido pedido, Produto produto, Integer quantidade) {
        this.pedido = pedido;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = produto.getPrecoAtual();
        this.nomeProduto = produto.getNome();
        calcularSubtotal();
    }

    // Business methods
    public void calcularSubtotal() {
        this.subtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) {
        this.produto = produto;
        this.precoUnitario = produto.getPrecoAtual();
        this.nomeProduto = produto.getNome();
    }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
        calcularSubtotal();
    }

    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
        calcularSubtotal();
    }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public String getNomeProduto() { return nomeProduto; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }
}