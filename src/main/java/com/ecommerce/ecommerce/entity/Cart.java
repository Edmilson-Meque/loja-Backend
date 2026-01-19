package com.ecommerce.ecommerce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrinhos")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", unique = true, nullable = false)
    private User usuario;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao = LocalDateTime.now();

    @OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> itens = new ArrayList<>();

    // Constructor
    public Cart() {}

    public Cart(User usuario) {
        this.usuario = usuario;
    }

    // Business methods
    public BigDecimal getTotal() {
        return itens.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Integer getQuantidadeTotal() {
        return itens.stream()
                .mapToInt(CartItem::getQuantidade)
                .sum();
    }

    public void atualizarData() {
        this.dataAtualizacao = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUsuario() { return usuario; }
    public void setUsuario(User usuario) { this.usuario = usuario; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public List<CartItem> getItens() { return itens; }
    public void setItens(List<CartItem> itens) { this.itens = itens; }
}