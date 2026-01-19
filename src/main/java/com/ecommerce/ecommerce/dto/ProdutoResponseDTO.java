package com.ecommerce.ecommerce.dto;

import java.util.List;

public class ProdutoResponseDTO {

    private Long id;
    private String nome;
    private String shortDescription;
    private String longDescription;
    private Double preco;
    private Double precoPromocional;
    private Integer quantidadeEstoque;
    private boolean emPromocao;
    private boolean ofertaDoDia;
    private boolean emDestaque;
    private String sku;
    private Long categoriaId;
    private String categoriaNome;
    private Double precoAtual;
    private Integer percentualDesconto;

    private Boolean favoritado = false;
    private String imagemPrincipal;
    private Integer totalImagens;
    private List<String> imagensUrls;
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public boolean isEmPromocao() { return emPromocao; }
    public void setEmPromocao(boolean emPromocao) { this.emPromocao = emPromocao; }

    public boolean isOfertaDoDia() { return ofertaDoDia; }
    public void setOfertaDoDia(boolean ofertaDoDia) { this.ofertaDoDia = ofertaDoDia; }

    public boolean isEmDestaque() { return emDestaque; }
    public void setEmDestaque(boolean emDestaque) { this.emDestaque = emDestaque; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }

    public String getCategoriaNome() { return categoriaNome; }
    public void setCategoriaNome(String categoriaNome) { this.categoriaNome = categoriaNome; }

    public Double getPrecoAtual() { return precoAtual; }
    public void setPrecoAtual(Double precoAtual) { this.precoAtual = precoAtual; }

    public Integer getPercentualDesconto() { return percentualDesconto; }
    public void setPercentualDesconto(Integer percentualDesconto) { this.percentualDesconto = percentualDesconto; }
    public Boolean getFavoritado() { return favoritado; }
    public void setFavoritado(Boolean favoritado) { this.favoritado = favoritado; }
    public String getImagemPrincipal() { return imagemPrincipal; }
    public void setImagemPrincipal(String imagemPrincipal) { this.imagemPrincipal = imagemPrincipal; }

    public Integer getTotalImagens() { return totalImagens; }
    public void setTotalImagens(Integer totalImagens) { this.totalImagens = totalImagens; }

    public List<String> getImagensUrls() { return imagensUrls; }
    public void setImagensUrls(List<String> imagensUrls) { this.imagensUrls = imagensUrls; }
}