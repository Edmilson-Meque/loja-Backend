package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoryRequestDTO {

    @NotBlank(message = "Nome da categoria é obrigatório")
    private String nome;

    private String descricao;
    private String imagemUrl;

    // Getters and Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }
}