package com.ecommerce.ecommerce.dto;

public class CategoryResponseDTO {

    private Long id;
    private String nome;
    private String descricao;
    private String imagemUrl;
    private Integer quantidadeProdutos;

    // Constructor
    public CategoryResponseDTO() {}

    public CategoryResponseDTO(Long id, String nome, String descricao, String imagemUrl, Integer quantidadeProdutos) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.imagemUrl = imagemUrl;
        this.quantidadeProdutos = quantidadeProdutos;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }

    public Integer getQuantidadeProdutos() { return quantidadeProdutos; }
    public void setQuantidadeProdutos(Integer quantidadeProdutos) { this.quantidadeProdutos = quantidadeProdutos; }
}