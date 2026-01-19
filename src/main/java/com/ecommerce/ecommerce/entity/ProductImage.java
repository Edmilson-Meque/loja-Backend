package com.ecommerce.ecommerce.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "produto_imagens")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url_imagem", nullable = false)
    private String urlImagem;

    @Column(name = "ordem")
    private Integer ordem;

    @Column(name = "principal")
    private boolean principal = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    // Constructor
    public ProductImage() {}

    public ProductImage(String urlImagem, Produto produto) {
        this.urlImagem = urlImagem;
        this.produto = produto;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUrlImagem() { return urlImagem; }
    public void setUrlImagem(String urlImagem) { this.urlImagem = urlImagem; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public boolean isPrincipal() { return principal; }
    public void setPrincipal(boolean principal) { this.principal = principal; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
}