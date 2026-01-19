package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.*;

public class ProdutoRequestDTO {

    @NotBlank(message = "Nome do produto é obrigatório")
    private String nome;

    @Size(max = 255)
    private String shortDescription;

    private String longDescription;

    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser positivo")
    private Double preco;

    private Double precoPromocional;

    @NotNull(message = "Quantidade em estoque é obrigatória")
    @Min(value = 0, message = "Estoque não pode ser negativo")
    private Integer quantidadeEstoque;

    @NotNull(message = "Categoria é obrigatória")
    private Long categoriaId;

    private String sku;
    private Boolean ofertaDoDia = false;
    private Boolean emDestaque = false;

    // Getters and Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public String getLongDescription() { return longDescription; }
    public void setLongDescription(String longDescription) { this.longDescription = longDescription; }

    public Double getPreco() { return preco; }
    public void setPreco(Double preco) { this.preco = preco; }

    public Double getPrecoPromocional() { return precoPromocional; }
    public void setPrecoPromocional(Double precoPromocional) { this.precoPromocional = precoPromocional; }

    public Integer getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(Integer quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }

    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Boolean isOfertaDoDia() { return ofertaDoDia; }
    public void setOfertaDoDia(Boolean ofertaDoDia) { this.ofertaDoDia = ofertaDoDia; }

    public Boolean isEmDestaque() { return emDestaque; }
    public void setEmDestaque(Boolean emDestaque) { this.emDestaque = emDestaque; }
}