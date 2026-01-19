package com.ecommerce.ecommerce.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "favoritos",
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "produto_id"}))
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(name = "data_adicao")
    private LocalDateTime dataAdicao = LocalDateTime.now();

    // Constructor
    public Favorite() {}

    public Favorite(User usuario, Produto produto) {
        this.usuario = usuario;
        this.produto = produto;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUsuario() { return usuario; }
    public void setUsuario(User usuario) { this.usuario = usuario; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public LocalDateTime getDataAdicao() { return dataAdicao; }
    public void setDataAdicao(LocalDateTime dataAdicao) { this.dataAdicao = dataAdicao; }
}