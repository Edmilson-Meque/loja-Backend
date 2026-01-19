package com.ecommerce.ecommerce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "short_description", length = 255)
    private String shortDescription;

    @Lob
    @Column(name = "long_description")
    private String longDescription;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(name = "preco_promocional", precision = 10, scale = 2)
    private BigDecimal precoPromocional;

    // MUDAR boolean para Boolean
    @Column(name = "em_promocao")
    private Boolean emPromocao = false;

    @Column(name = "oferta_do_dia")
    private Boolean ofertaDoDia = false;

    @Column(name = "em_destaque")
    private Boolean emDestaque = false;

    @Column(name = "quantidade_estoque")
    private Integer quantidadeEstoque;

    @Column(name = "sku", unique = true)
    private String sku;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = true)
    private Category categoria;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> imagens = new ArrayList<>();

    // Constructor
    public Produto() {}

    // ✅ Atualize o construtor para usar Boolean
    public Produto(String nome, String shortDescription, String longDescription,
                   BigDecimal preco, Integer quantidadeEstoque, Category categoria) {
        this.nome = nome;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
        this.categoria = categoria;
        this.emPromocao = false;
        this.ofertaDoDia = false;
        this.emDestaque = false;
    }

    // Business methods
    public BigDecimal getPrecoAtual() {
        return Boolean.TRUE.equals(emPromocao) && precoPromocional != null ?
                precoPromocional : preco;
    }

    public Integer getPercentualDesconto() {
        if (Boolean.TRUE.equals(emPromocao) && precoPromocional != null &&
                preco.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal desconto = preco.subtract(precoPromocional);
            return desconto.divide(preco, 2, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .intValue();
        }
        return 0;
    }

    // ✅ Atualize todos os getters/setters para Boolean
    public Boolean getEmPromocao() { return emPromocao; }
    public void setEmPromocao(Boolean emPromocao) { this.emPromocao = emPromocao; }

    public Boolean getOfertaDoDia() { return ofertaDoDia; }
    public void setOfertaDoDia(Boolean ofertaDoDia) { this.ofertaDoDia = ofertaDoDia; }

    public Boolean getEmDestaque() { return emDestaque; }
    public void setEmDestaque(Boolean emDestaque) { this.emDestaque = emDestaque; }

    // Helper methods para compatibilidade
    public boolean isEmPromocao() { return Boolean.TRUE.equals(emPromocao); }
    public boolean isOfertaDoDia() { return Boolean.TRUE.equals(ofertaDoDia); }
    public boolean isEmDestaque() { return Boolean.TRUE.equals(emDestaque); }

    // ... outros getters/setters permanecem iguais

    public List<ProductImage> getImagens() {
        return imagens;
    }

    public void setImagens(List<ProductImage> imagens) {
        this.imagens = imagens;
    }

    public Category getCategoria() {
        return categoria;
    }

    public void setCategoria(Category categoria) {
        this.categoria = categoria;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public BigDecimal getPrecoPromocional() {
        return precoPromocional;
    }

    public void setPrecoPromocional(BigDecimal precoPromocional) {
        this.precoPromocional = precoPromocional;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}